#!/bin/bash

set -e

TARGET="http://localhost:8080"
INPUT_PATH=../voting-data/data/output/plenary/json/plenaries.json

jq -c '.[]' "$INPUT_PATH" | while IFS= read -r obj; do
    echo "$obj" | curl --fail -H"Content-Type: application/json" --data-binary "@-" "$TARGET/plenaries"
done


