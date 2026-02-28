# Kino App Einfach – vereinfachte Variante

Native Android-Kino-App in **Jetpack Compose**, bewusst **ohne Threading** (kein Executor, keine Hintergrund-Threads). Alle Datenbankzugriffe laufen auf dem Main-Thread (Room mit `allowMainThreadQueries()`). Geeignet für Kurse, in denen Threading noch nicht behandelt wird.

## Anforderungen (erfüllt)

- **Splash:** Logo (Hochschule Harz), Text „Kino App wird geladen…“, Ladeanimation (ca. 3 s), danach Filmliste
- **Filmliste:** Film-Cards mit **Icon** links (Filmbild oder Film-Platzhalter-Icon, 56×56 dp abgerundet), Titel, Datum, Uhrzeit, Genre, Saal, Beschreibung; **Suche/Filter** (Titel, Genre, Beschreibung); FAB „+“, Löschen-Button pro Card
- **Film hinzufügen:** Dialog „Film hinzufügen“ mit **Outlined TextFields** für: **Datum** (TT.MM.JJJJ), **Uhrzeit** (HH:MM), **Titel** *, **Beschreibung**, **Genre**, **Kinosaal**, optional Button „Bild auswählen“; Buttons „Abbrechen“ und „Speichern“; Pflichtfelder: Datum, Uhrzeit, Titel (keine DatePicker/TimePicker)
- **Film löschen:** Löschen-Button auf der Card → Bestätigungsdialog
- **Persistenz:** Room (lokal), LiveData im Repository, kein Flow/suspend
- **Impressum:** Eigener Screen mit Platzhalter-Text
- **Navigation:** Bottom Navigation („Filme“, „Infos“); Routen: startseite → filmliste, info

## Technologie

- Kotlin, minSdk 24, targetSdk 34
- **Jetpack Compose** (Material3), Navigation Compose, ViewModel, **Room** mit `allowMainThreadQueries()`
- **Kein Threading:** Repository ruft DAO direkt auf und aktualisiert nach insert/update/delete die Liste per `aktualisieren()` (`alleFilmeListe`)
- Coil für Bildanzeige; Filmbilder als Icon in der Kachel (ohne Bild: Film-Icon als Platzhalter)

## Projekt öffnen und bauen

1. **Android Studio** öffnen → „Open“ → Ordner `KinoAppEinfach` wählen
2. Gradle-Sync abwarten
3. **Run** oder **Build → Build APK(s)**

APK: `app/build/outputs/apk/debug/`

## Impressum anpassen

In `app/src/main/java/de/hs/harz/kinoapp/ui/compose/ImpressumScreen.kt` die Platzhalter „[Ihr Name]“, „[Studiengang]“, „[mXXXXX]“ durch deine Angaben ersetzen.

## Implementierungsdetails (KinoAppEinfach)

- **Application:** `KinoAppInstanz` (KinoApplication.kt), im Manifest als `android:name=".KinoAppInstanz"`; App-Label „Meine Kino App“.
- **Datenbank:** `KinoDatabase`, `getDatabase(context)`, DB-Name „filme_db_final“, `allowMainThreadQueries()`; DAO: `holeAlleFilme()`, `filmEinfuegen()`, `loescheFilm()` usw.; Repository: `alleFilmeListe`, `aktualisieren()`.
- **UI:** Theme `KinoDesign` (Theme.kt), Haupt-Composable `MeineKinoApp()` (KinoApp.kt); NavHost-Routen: `startseite`, `filmliste`, `info`; Splash-Parameter `weiterleiten`.

## Bezug zur Dokumentation

- **Durchführungsplan:** Phase 2–5 beschreiben Splash (2,5–3 s), Persistenz (KinoDatabase, aktualisieren()), Navigation (startseite/filmliste/info, „Filme“/„Infos“).
- **Figma-Design-Spezifikation:** Abschnitt 7 verweist auf KinoAppEinfach (KinoDatabase, KinoAppInstanz, MeineKinoApp, KinoDesign, Routen, Dialogfelder).
