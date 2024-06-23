#!/bin/bash

set -e

TARGET="http://localhost:8080"

function upload_summaries() { 
    INPUT_PATH="../voting-data/data/output/plenary/json/summaries.json"

    jq -c '.[]' "$INPUT_PATH" | while IFS= read -r obj; do
        echo "$obj" | curl --fail -H"Content-Type: application/json" --data-binary "@-" "$TARGET/document-summaries"
    done
}

function upload_summaries_bulk() { 
    INPUT_PATH="../voting-data/data/output/documents/summaries.json"

    curl --fail -H"Content-Type: application/json" --data-binary "@${INPUT_PATH}" "$TARGET/document-summaries/bulk"
}

#upload_summaries
upload_summaries_bulk
