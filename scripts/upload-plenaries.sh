#!/bin/bash

set -e

WDDP_BACKEND_URL="${WDDP_BACKEND_URL-:http://localhost:8080}"

TARGET="${WDDP_BACKEND_URL}"
INPUT_PATH=../voting-data/data/output/plenary/json/plenaries.json

function upload_plenaries() {
  echo "Uploading plenaries"
  count=0
  jq -c '.[]' "$INPUT_PATH" | while IFS= read -r obj; do
      echo "Uploading plenary ($[count++])"
      echo "$obj" | curl --fail -H"Content-Type: application/json" --data-binary "@-" "$TARGET/plenaries"
  done
}

upload_plenaries

