# LottoArchiveApp (Android, Kotlin + Compose)

Funzioni principali:
- Archivio estrazioni (Room DB) con esempio incluso in `assets/sample_lotto.csv`.
- Ricerca per ruota e/o numero.
- Console script: JavaScript nativo (Rhino). Modalità VBS (beta) con una conversione minima per script semplici.

## Come usare
1. Apri la cartella in Android Studio (Gira con JDK 17).
2. Sincronizza Gradle e avvia l'app su un emulatore/dispositivo.
3. `Archivio` → Carica dati di esempio.
4. `Cerca` → Filtra per ruota/numero.
5. `Script` → scrivi JS, ad es.:
   ```js
   print(countOccurrences(90, "Bari"))
   randomNumbers(5)
   ```
   Per VBS (beta):
   ```vb
   ' conta i 90 su Bari
   MsgBox countOccurrences(90, "Bari")
   ```

## Import CSV reali
- Sostituisci `assets/sample_lotto.csv` con un dataset completo **(stesso schema: date,wheel,n1..n5)**.
- In produzione puoi aggiungere un picker file (Storage Access Framework) per importare CSV dall'utente.

## Nota su VBScript
Android non esegue VBS nativamente. Questa app usa **Rhino (JS)**
e fornisce una *shim* VBS→JS **molto basilare** (commenti `'`, `MsgBox`).
Per script VBS complessi, consigliabile convertirli manualmente in JS.

## Licenza
MIT (usa liberamente).


## Novità
- **Import CSV (SAF)**: Tab *Importa* → scegli un file CSV dal dispositivo.
- **Statistiche**: Tab *Statistiche* → frequenze, ultima uscita, top 10, conteggio combinazioni (ambi/terni).
- **Script potenziati**: nuove funzioni `frequency`, `lastSeen`, `topFrequencies`, `comboFrequency`. Migliorata compatibilità VBS (MsgBox/WScript.Echo, &, CInt/CLng, Split, UBound/LBound, Dim/Set).

### Guida rapida VBS → JS
- `MsgBox x` → `print(x)`
- Concatenazione: `a & b` → `a + b`
- `CInt(x)` / `CLng(x)` → `parseInt(x)`
- `Split("a,b", ",")` → `String("a,b").split(",")`
- `UBound(arr)` → `arr.length-1`, `LBound(arr)` → `0`
- Parole chiave ignorate: `Dim`, `Set`
- Funzioni disponibili: `countOccurrences(num, wheel)`, `frequency(num, wheel)`, `lastSeen(num, wheel)`, `topFrequencies(n, wheel)`, `comboFrequency("1,2,3", wheel)`



## Build APK su Android (Termux)
1. Installa Termux (F-Droid). Poi:
   ```bash
   pkg update && pkg upgrade -y
   pkg install openjdk-17 git unzip wget -y
   ```
2. Entra nella cartella del progetto e genera la **chiave personale**:
   ```bash
   cd LottoArchiveApp/scripts
   bash create_keystore.sh
   ```
3. Torna al root e compila **release** firmata:
   ```bash
   cd ..
   ./gradlew assembleRelease
   ```
4. L'APK sarà in: `app/build/outputs/apk/release/app-release.apk`.

### Password/alias preconfigurati (puoi cambiarli in create_keystore.sh)
- Alias: ENGYX
- Password: Lottosmart108821!!

