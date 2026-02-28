/**
 * ViewModel der App (MVVM).
 *
 * Aufgaben: UI-Zustand (Liste der Filme) halten und Funktionen bereitstellen,
 * die die UI auslösen kann – Hinzufügen und Löschen. Die eigentliche DB-Arbeit
 * macht das Repository, hier nur Orchestrierung.
 * Compose beobachtet `filme` – bei Änderung recomposed die UI automatisch.
 */
package de.hs.harz.kinoapp.ui.main

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import de.hs.harz.kinoapp.data.FilmRepository
import de.hs.harz.kinoapp.data.KinoDatabase
import de.hs.harz.kinoapp.model.Film

// AndroidViewModel – wir brauchen den Application-Context für die DB-Initialisierung
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = FilmRepository(KinoDatabase.getDatabase(app).filmDao())

    // Compose beobachtet diesen State – bei Änderung wird die UI neu gerendert
    var filme: List<Film> by mutableStateOf(emptyList())
        private set

    init {
        // Beim Start direkt aus der DB laden
        reload()
    }

    fun filmHinzufuegen(film: Film) {
        repo.insert(film)
        // Danach neu laden – so sind die IDs und die Reihenfolge garantiert korrekt
        reload()
    }

    fun filmLoeschen(film: Film) {
        repo.delete(film)
        reload()
    }

    private fun reload() {
        filme = repo.loadAll()
    }
}
