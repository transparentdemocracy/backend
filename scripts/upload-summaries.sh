#!/bin/bash

set -e

WDDP_BACKEND_URL="${WDDP_BACKEND_URL-:http://localhost:8080}"

TARGET="${WDDP_BACKEND_URL}"

function upload_summaries_bulk() { 
    INPUT_PATH="../voting-data/data/output/documents/summaries.json"

    echo "Uploading summaries"
    curl --fail -H"Content-Type: application/json" --data-binary "@${INPUT_PATH}" "$TARGET/document-summaries/bulk"
}

upload_summaries_bulk
