#!/bin/bash

set -e

WDDP_BACKEND_URL="${WDDP_BACKEND_URL-:http://localhost:8080}"

TARGET="${WDDP_BACKEND_URL}"

function upload_politicians() { 
    INPUT_PATH="../voting-data/data/output/politician/politicians.json"

    echo "Uploading politicians"
    jq -c '.[]' "$INPUT_PATH" | while IFS= read -r obj; do
        echo "$obj" | curl --fail -H"Content-Type: application/json" --data-binary "@-" "$TARGET/politicians"
    done
}

upload_politicians
