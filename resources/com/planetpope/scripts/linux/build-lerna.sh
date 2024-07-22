#!/bin/bash

# File to check
FILE="lerna.json"

# Check if file exists
if [ -f "$FILE" ]; then
    echo "File $FILE exists."
else
    echo "File $FILE does not exist."
fi
