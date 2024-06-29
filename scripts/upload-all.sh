#!/bin/bash

set -e -x

scripts/upload-politicians.sh
scripts/upload-summaries.sh
scripts/upload-votes.sh
scripts/upload-plenaries.sh
