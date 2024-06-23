#!/bin/bash

set -e

TARGET="http://localhost:8080"

function upload_politicians() { 

    INPUT_PATH="../voting-data/data/output/politician/politicians.json"

    jq -c '.[]' "$INPUT_PATH" | while IFS= read -r obj; do
        echo "$obj" | curl --fail -H"Content-Type: application/json" --data-binary "@-" "$TARGET/politicians"
    done
}

upload_politicians
