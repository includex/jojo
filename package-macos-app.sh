#!/bin/sh
set -eu

PROJECT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
APP_NAME="JojoGame"
OUTPUT_DIR="$PROJECT_DIR/build/package"
APP_PATH="$OUTPUT_DIR/$APP_NAME.app"

"$PROJECT_DIR/gradlew" :desktop:installDist
mkdir -p "$OUTPUT_DIR"
if [ -d "$APP_PATH" ]; then
  BACKUP_PATH="$OUTPUT_DIR/$APP_NAME.app.previous-$(date +%Y%m%d%H%M%S)"
  mv "$APP_PATH" "$BACKUP_PATH"
  echo "Preserved previous app: $BACKUP_PATH"
fi

jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --app-version 1.0.0 \
  --vendor "SGCCZ" \
  --dest "$OUTPUT_DIR" \
  --input "$PROJECT_DIR/desktop/build/install/desktop/lib" \
  --main-jar desktop-0.1.0.jar \
  --main-class com.jojo.game.desktop.DesktopLauncher \
  --java-options=-XstartOnFirstThread \
  --java-options=--enable-native-access=ALL-UNNAMED

echo "Created: $APP_PATH"
