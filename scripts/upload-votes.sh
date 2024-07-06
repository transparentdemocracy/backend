#!/bin/bash

set -e

WDDP_BACKEND_URL="${WDDP_BACKEND_URL-:http://localhost:8080}"

TARGET="${WDDP_BACKEND_URL}"

function upload_votes_bulk() { 
    INPUT_PATH="../voting-data/data/output/plenary/json/votes.json"

    echo "Uploading votes"
    curl --fail -H"Content-Type: application/json" --data-binary "@${INPUT_PATH}" "$TARGET/votes/bulk"
}

upload_votes_bulk
