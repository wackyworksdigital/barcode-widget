# Barcode Widget

A deliberately tiny Android home-screen widget that displays barcodes. No settings, no
permissions, no network — just barcodes.

## How it works

1. Long-press your home screen → **Widgets** → add **Barcode Widget**.
2. The widget has two slots, **top** and **bottom**.
3. Tap a slot, type any code (numbers, letters, symbols) and press Enter.
4. The slot renders your text as a scannable **Code 128** barcode, with the value shown
   underneath.

Each slot is independent, so you can keep e.g. a location barcode and a product barcode
visible at the same time. Values persist across reboots and are stored only on the device.
Entering an empty value clears a slot.

## Tech notes

- Kotlin, no AndroidX dependencies — just `com.google.zxing:core` for barcode encoding.
- `minSdk 26`, `targetSdk 35`.
- Widget is an `AppWidgetProvider` with `updatePeriodMillis = 0` (no background work at
  all); taps open a small dialog activity that saves to `SharedPreferences` and pushes a
  `RemoteViews` update.

## Building

Open in Android Studio, or from the command line:

```sh
./gradlew assembleDebug
```

The Android SDK (platform 35, build-tools 35.0.0) must be installed and referenced from
`local.properties` or `ANDROID_HOME`. In Claude Code on the web, the SessionStart hook in
`.claude/hooks/session-start.sh` sets this up automatically.

## Play Store

Before publishing: create a release keystore, add a `signingConfig` for the release build
type, and bump `versionCode` per release. Build with `./gradlew bundleRelease` to produce
the `.aab` Play Console expects.
