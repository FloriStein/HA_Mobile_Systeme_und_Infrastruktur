/**
 * Repository-Schicht (MVVM) – Vermittler zwischen ViewModel und DB.
 *
 * In größeren Apps würde man hier Caching, mehrere Datenquellen etc. einbauen.
 * Für die Hausarbeit: schlanker Wrapper um den DAO, keine Coroutines/Flow.
 */
package de.hs.harz.kinoapp.data

import de.hs.harz.kinoapp.model.Film

// Dünner Wrapper um den DAO – nur synchrone Calls, passend für die Hausarbeit
class FilmRepository(private val dao: FilmDao) {

    // Liefert alle Filme – sortiert wie im DAO definiert
    fun loadAll(): List<Film> = dao.holeAlleFilme()

    fun insert(film: Film) {
        dao.filmEinfuegen(film)
    }

    fun delete(film: Film) {
        dao.loescheFilm(film)
    }
}

