#!/data/data/com.termux/files/usr/bin/bash
set -e
# Vai nella cartella principale del progetto
cd "$(dirname "$0")/.."
# Lancia gradlew da qui
bash ./gradlew assembleRelease
APK="app/build/outputs/apk/release/app-release.apk"
echo "APK generato: $APK"
