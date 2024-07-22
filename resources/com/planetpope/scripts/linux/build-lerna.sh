#!/bin/bash

# File to check
FILE="lerna.json"

# Check if file exists
if [ -f "$FILE" ]; then
    echo "File $FILE exists."
    
    # Get the list of changed packages in the workspaces directory
    CHANGED_PACKAGES=$(git diff --name-only HEAD HEAD~1 | grep '^packages/' | awk -F/ '{print $2}' | sort | uniq)
    
    if [ -z "$CHANGED_PACKAGES" ]; then
        echo "No changes detected in the workspaces directory."
    else
        echo "Changed packages:"
        echo "$CHANGED_PACKAGES"
        
        # Build each changed package with Lerna
        for PACKAGE in $CHANGED_PACKAGES; do
            echo "Building package: $PACKAGE"
            npx lerna run build --scope=$PACKAGE
        done
    fi
else
    echo "File $FILE does not exist."
fi