import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiveAirTollgateServer {

    static class RegionBox {
        final String code, name;
        final double minLat, maxLat, minLon, maxLon, toll;

        RegionBox(String code, String name, double minLat, double maxLat, double minLon, double maxLon, double toll) {
            this.code = code;
            this.name = name;
            this.minLat = minLat;
            this.maxLat = maxLat;
            this.minLon = minLon;
            this.maxLon = maxLon;
            this.toll = toll;
        }

        boolean contains(double lat, double lon) {
            return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
        }
    }

    private static final List<RegionBox> REGIONS = new ArrayList<>();
    private static final Map<String, String> activeRegionByAircraft = new ConcurrentHashMap<>();
    private static final List<String> detectionEventsJson = new CopyOnWriteArrayList<>();
    private static volatile double totalTollCollected = 0.0;
    private static volatile boolean running = true;

    public static void main(String[] args) throws Exception {
        loadRegionsConfig(Path.of("regions.properties"));

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        long pollIntervalMs = Long.parseLong(System.getenv().getOrDefault("POLL_INTERVAL_MS", "15000"));

        startHttpServer(port);
        System.out.println("Server running on port " + port);

        HttpClient client = HttpClient.newHttpClient();
        String apiUrl = System.getenv().getOrDefault(
                "OPENSKY_URL",
                "https://opensky-network.org/api/states/all?lamin=12.0&lomin=76.5&lamax=18.5&lomax=79.5"
        );

        // J-loop (do-while): polls live aircraft feed and checks region entry tolls
        do {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(apiUrl))
                        .GET()
                        .build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() == 200) {
                    processStatesJson(res.body());
                } else {
                    System.out.println("OpenSky HTTP status: " + res.statusCode());
                }
            } catch (Exception e) {
                System.out.println("Poll error: " + e.getMessage());
            }

            Thread.sleep(pollIntervalMs);
        } while (running);
    }

    private static void loadRegionsConfig(Path path) throws IOException {
        Properties props = new Properties();
        if (Files.exists(path)) {
            props.load(Files.newBufferedReader(path));
        }
        String codesCsv = props.getProperty("regions", "HYD,BLR");
        for (String rawCode : codesCsv.split(",")) {
            String c = rawCode.trim();
            if (c.isEmpty()) continue;
            REGIONS.add(new RegionBox(
                    c,
                    props.getProperty(c + ".name", c),
                    Double.parseDouble(props.getProperty(c + ".minLat")),
                    Double.parseDouble(props.getProperty(c + ".maxLat")),
                    Double.parseDouble(props.getProperty(c + ".minLon")),
                    Double.parseDouble(props.getProperty(c + ".maxLon")),
                    Double.parseDouble(props.getProperty(c + ".toll"))
            ));
        }
    }

    private static void processStatesJson(String json) {
        int statesIdx = json.indexOf("\"states\"");
        if (statesIdx == -1) return;
        int arrStart = json.indexOf('[', statesIdx);
        if (arrStart == -1) return;

        List<String> rows = splitTopLevelArrays(json.substring(arrStart));
        Set<String> seenAircraft = new HashSet<>();

        for (String row : rows) {
            List<String> fields = splitTopLevelValues(row);
            if (fields.size() < 8) continue;

            String icao24 = unquote(fields.get(0)).trim();
            String callsign = unquote(fields.get(1)).trim();
            Double lon = parseNullableDouble(fields.get(5));
            Double lat = parseNullableDouble(fields.get(6));
            Double alt = parseNullableDouble(fields.get(7));

            if (icao24.isEmpty() || lat == null || lon == null) continue;
            seenAircraft.add(icao24);

            RegionBox matched = null;
            for (RegionBox r : REGIONS) {
                if (r.contains(lat, lon)) {
                    matched = r;
                    break;
                }
            }

            String prevRegion = activeRegionByAircraft.get(icao24);
            if (matched != null) {
                if (!matched.code.equals(prevRegion)) {
                    activeRegionByAircraft.put(icao24, matched.code);
                    totalTollCollected += matched.toll;

                    String label = callsign.isEmpty() ? icao24.toUpperCase() : callsign;
                    String eventJson = String.format(
                            Locale.US,
                            "{\"time\":\"%s\",\"icao24\":\"%s\",\"callsign\":\"%s\",\"region\":\"%s\",\"regionName\":\"%s\",\"lat\":%.4f,\"lon\":%.4f,\"alt\":%.0f,\"toll\":%.2f}",
                            Instant.now().toString(),
                            escapeJson(icao24),
                            escapeJson(label),
                            escapeJson(matched.code),
                            escapeJson(matched.name),
                            lat,
                            lon,
                            alt == null ? 0.0 : alt,
                            matched.toll
                    );
                    detectionEventsJson.add(0, eventJson);
                    System.out.printf("DETECTED: %s (%s) entered %s | Toll +%.2f | Total: %.2f%n",
                            label, icao24, matched.code, matched.toll, totalTollCollected);
                }
            } else {
                activeRegionByAircraft.remove(icao24);
            }
        }

        activeRegionByAircraft.keySet().retainAll(seenAircraft);
    }

    private static void startHttpServer(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/detections", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String body = String.format(
                    Locale.US,
                    "{\"totalTollCollected\":%.2f,\"activeTracked\":%d,\"detections\":[%s]}",
                    totalTollCollected,
                    activeRegionByAircraft.size(),
                    String.join(",", detectionEventsJson)
            );
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.createContext("/detections.html", exchange -> {
            Path htmlPath = Path.of("detections.html");
            byte[] bytes = Files.exists(htmlPath)
                    ? Files.readAllBytes(htmlPath)
                    : "<h1>detections.html missing</h1>".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        });

        server.start();
    }

    private static List<String> splitTopLevelArrays(String s) {
        List<String> out = new ArrayList<>();
        int depth = 0, start = -1;
        boolean inQuotes = false, escape = false;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (escape) { escape = false; continue; }
            if (ch == '\\') { escape = true; continue; }
            if (ch == '"') { inQuotes = !inQuotes; continue; }
            if (inQuotes) continue;
            if (ch == '[') {
                if (depth == 1) start = i;
                depth++;
            } else if (ch == ']') {
                depth--;
                if (depth == 1 && start != -1) {
                    out.add(s.substring(start, i + 1));
                    start = -1;
                } else if (depth == 0) {
                    break;
                }
            }
        }
        return out;
    }

    private static List<String> splitTopLevelValues(String arrayStr) {
        List<String> vals = new ArrayList<>();
        String inner = arrayStr.trim();
        if (inner.startsWith("[") && inner.endsWith("]")) {
            inner = inner.substring(1, inner.length() - 1);
        }
        int depth = 0, start = 0;
        boolean inQuotes = false, escape = false;
        for (int i = 0; i < inner.length(); i++) {
            char ch = inner.charAt(i);
            if (escape) { escape = false; continue; }
            if (ch == '\\') { escape = true; continue; }
            if (ch == '"') { inQuotes = !inQuotes; continue; }
            if (inQuotes) continue;
            if (ch == '[' || ch == '{') depth++;
            else if (ch == ']' || ch == '}') depth--;
            else if (ch == ',' && depth == 0) {
                vals.add(inner.substring(start, i).trim());
                start = i + 1;
            }
        }
        if (start <= inner.length()) vals.add(inner.substring(start).trim());
        return vals;
    }

    private static Double parseNullableDouble(String raw) {
        if (raw == null || raw.equals("null") || raw.isEmpty()) return null;
        return Double.parseDouble(raw);
    }

    private static String unquote(String raw) {
        String t = raw.trim();
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
