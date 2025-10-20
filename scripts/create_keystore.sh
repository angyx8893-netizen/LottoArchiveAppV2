#!/data/data/com.termux/files/usr/bin/bash
set -e
cd "$(dirname "$0")"
APP_DIR="$(cd .. && pwd)"
KS_DIR="$APP_DIR/app/keystore"
mkdir -p "$KS_DIR"

ALIAS="ENGYX"
PASS="Lottosmart108821!!"
DNAME="CN=Angelo Mancarella, OU=Dev, O=Angelo Mancarella, L=Siracusa, ST=Italia, C=IT"

keytool -genkeypair -v -keystore "$KS_DIR/lotto_keystore.jks" -alias "$ALIAS" -keyalg RSA -keysize 2048 -validity 9125 -storepass "$PASS" -keypass "$PASS" -dname "$DNAME"

# Gradle properties for passwords (optional)
cat > "$APP_DIR/gradle.properties" <<EOF
LOTTO_KEYSTORE_PASSWORD=$PASS
LOTTO_KEY_PASSWORD=$PASS
LOTTO_KEY_ALIAS=$ALIAS
EOF

echo "Keystore creato in $KS_DIR/lotto_keystore.jks"
