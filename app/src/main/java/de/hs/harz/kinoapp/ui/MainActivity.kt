/**
 * Modul: MainActivity
 * Einstiegspunkt der App
 *
 * Es gibt kein XML-Layout die komplette Oberfläche wird mit Jetpack Compose gebaut
 * KinoDesign wrappet alles mit dem dunklen Theme MeineKinoApp kümmert sich um
 * Navigation und die eigentlichen Screens
 */
package de.hs.harz.kinoapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.hs.harz.kinoapp.ui.compose.MeineKinoApp
import de.hs.harz.kinoapp.ui.compose.KinoDesign

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("KinoApp", "MainActivity gestartet")

        // Kein XML-Layout alles läuft über Compose
        setContent {
            KinoDesign {
                MeineKinoApp()
            }
        }
    }
}
