#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

echo "Stopping local services and removing this project's Docker Compose volumes..."
docker compose down -v

echo "Starting local dependencies..."
docker compose up -d

echo "Local database reset complete."
