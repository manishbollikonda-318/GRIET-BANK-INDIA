#!/usr/bin/env bash
set -e

# Compile if bin directory does not exist or is empty
if [ ! -d "bin" ] || [ -z "$(ls -A bin 2>/dev/null)" ]; then
    bash build.sh
fi

echo "=========================================="
echo " Launching Banking Management System...   "
echo " Default Admin Credentials: admin / admin "
echo "=========================================="

java -cp bin Application
