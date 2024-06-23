#!/bin/bash

set -e

TARGET="http://localhost:8080"

function upload_votes() { 
    INPUT_PATH="../voting-data/data/output/plenary/json/votes.json"

    jq -c '.[]' "$INPUT_PATH" | while IFS= read -r obj; do
        echo "$obj" | curl --fail -H"Content-Type: application/json" --data-binary "@-" "$TARGET/votes"
    done
}

function upload_votes_bulk() { 
    INPUT_PATH="../voting-data/data/output/plenary/json/votes.json"

    curl --fail -H"Content-Type: application/json" --data-binary "@${INPUT_PATH}" "$TARGET/votes/bulk"
}

#upload_votes
upload_votes_bulk
