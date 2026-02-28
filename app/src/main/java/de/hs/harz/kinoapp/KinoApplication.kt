/**
 * Modul: KinoApplication (KinoAppInstanz)
 * Application-Klasse der App
 *
 * Wird beim App-Start einmal erstellt siehe AndroidManifest xml über android name
 * Hier nutzen wir das um beim ersten Start Beispieldaten einzufügen damit man sofort etwas sieht
 */
package de.hs.harz.kinoapp

import android.app.Application
import android.util.Log
import de.hs.harz.kinoapp.data.KinoDatabase
import de.hs.harz.kinoapp.model.Film

// Muss im AndroidManifest als android name eingetragen sein
class KinoAppInstanz : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("KinoApp", "Application wurde gestartet")
        beispielDatenHinzufuegen()
    }

    // Beim ersten Start ein paar Filme reinladen damit die App nicht leer ist
    private fun beispielDatenHinzufuegen() {
        val db = KinoDatabase.getDatabase(this)
        val anzahl = db.filmDao().getAnzahlFilme()

        if (anzahl == 0) {
            Log.d("KinoApp", "DB leer, füge Beispielfilme ein")

            // Zwei Beispielfilme Die IDs werden von Room automatisch vergeben autoGenerate
            db.filmDao().filmEinfuegen(
                Film(
                    datum = "15.02.2026",
                    uhrzeit = "20:00",
                    titel = "Dune: Part Two",
                    beschreibung = "Paul Atreides verbündet sich mit den Fremen.",
                    genre = "Science-Fiction",
                    saal = "Saal 1"
                )
            )

            db.filmDao().filmEinfuegen(
                Film(
                    datum = "16.02.2026",
                    uhrzeit = "18:30",
                    titel = "Wicked",
                    beschreibung = "Die Vorgeschichte des Zauberers von Oz.",
                    genre = "Musical",
                    saal = "Saal 2"
                )
            )
        }
    }
}
