#!/bin/bash
set -euo pipefail

# Only needed in Claude Code on the web containers.
if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

SDK_DIR="$HOME/android-sdk"
CMDLINE_TOOLS="$SDK_DIR/cmdline-tools/latest"

# Install Android command-line tools if missing (idempotent).
if [ ! -x "$CMDLINE_TOOLS/bin/sdkmanager" ]; then
  mkdir -p "$SDK_DIR/cmdline-tools"
  curl -sSLo /tmp/cmdtools.zip \
    https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
  unzip -q /tmp/cmdtools.zip -d "$SDK_DIR/cmdline-tools"
  mv "$SDK_DIR/cmdline-tools/cmdline-tools" "$CMDLINE_TOOLS"
  rm /tmp/cmdtools.zip
fi

# Accept all SDK licenses non-interactively, then install required packages.
yes | "$CMDLINE_TOOLS/bin/sdkmanager" --licenses > /dev/null 2>&1 || true
"$CMDLINE_TOOLS/bin/sdkmanager" --install \
  "platforms;android-35" "build-tools;35.0.0" "platform-tools" > /dev/null

# Point Gradle at the SDK.
echo "sdk.dir=$SDK_DIR" > "$CLAUDE_PROJECT_DIR/local.properties"

# Persist environment for the rest of the session.
echo "export ANDROID_HOME=\"$SDK_DIR\"" >> "$CLAUDE_ENV_FILE"
echo "export PATH=\"\$PATH:$SDK_DIR/platform-tools\"" >> "$CLAUDE_ENV_FILE"

echo "Android SDK ready at $SDK_DIR"
