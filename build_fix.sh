#!/data/data/com.termux/files/usr/bin/bash
# 🔧 Script automatico per fixare e compilare LottoArchiveApp

echo "🔍 Controllo presenza di jitpack.io nel build.gradle..."
if ! grep -q "jitpack.io" build.gradle; then
  echo "➡️  Aggiungo il repository JitPack..."
  sed -i '/mavenCentral()/a \ \ \ \ maven { url "https://jitpack.io" }' build.gradle
fi

echo "🧹 Pulizia progetto..."
./gradlew clean || { echo "❌ Errore durante la pulizia"; exit 1; }

echo "⚙️ Compilazione in corso..."
./gradlew assembleDebug || { echo "❌ Errore nella compilazione"; exit 1; }

APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
DEST="/storage/emulated/0/Download/LottoArchiveApp.apk"

if [ -f "$APK_PATH" ]; then
  echo "📦 Copio APK nei Download..."
  cp "$APK_PATH" "$DEST"
  echo "✅ APK pronto in: $DEST"
else
  echo "❌ File APK non trovato. Qualcosa è andato storto."
fi
