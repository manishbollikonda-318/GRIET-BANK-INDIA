#!/usr/bin/env bash
set -e

echo "=========================================="
echo " Compiling Banking Management System...   "
echo "=========================================="

mkdir -p bin
javac -d bin $(find src -name "*.java")

echo "[✓] Compilation successful! Binaries placed in ./bin"
