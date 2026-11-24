FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY LiveAirTollgateServer.java regions.properties detections.html ./
RUN javac LiveAirTollgateServer.java

EXPOSE 8080
ENV PORT=8080
ENV POLL_INTERVAL_MS=15000

CMD ["java", "LiveAirTollgateServer"]
