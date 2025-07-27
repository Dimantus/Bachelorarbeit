#!/bin/bash

CONTAINER_NAME="chatbot-server-big"
OUTPUT_FILE="stats-log-stream.json"
DURATION=3600

echo "[" > "$OUTPUT_FILE"

START_TIME=$(date +%s)
FIRST=true

docker stats --format '{{json .}}' "$CONTAINER_NAME" |
while IFS= read -r line; do
    CURRENT_TIME=$(date +%s)
    ELAPSED=$((CURRENT_TIME - START_TIME))

    if [ "$ELAPSED" -ge "$DURATION" ]; then
        break
    fi

    TIMESTAMP=$(date +"%Y-%m-%dT%H:%M:%S.%3N%z")

    if [ "$FIRST" = true ]; then
        FIRST=false
    else
        echo "," >> "$OUTPUT_FILE"
    fi

    echo "{\"timestamp\":\"$TIMESTAMP\",\"stats\":$line}" >> "$OUTPUT_FILE"
done

echo "]" >> "$OUTPUT_FILE"