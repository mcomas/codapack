#!/bin/bash
create-dmg \
    --volname "CoDaPack" \
    --volicon "src/main/resources/icons.icns" \
    --background "src/main/resources/background.png" \
    --window-pos 200 120 \
    --window-size 800 500 \
    --icon-size 100 \
    --icon "CoDaPack.app" 200 200 \
    --app-drop-link 600 200 \
    "CoDaPack.dmg" \
    "CoDaPack.app"

