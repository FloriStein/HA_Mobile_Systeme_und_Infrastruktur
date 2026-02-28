/**
 * Modul: FilmRepository
 * Repository Wrapper um FilmDao synchrone Calls
 */
package de.hs.harz.kinoapp.data

import de.hs.harz.kinoapp.model.Film

class FilmRepository(private val dao: FilmDao) {

    // Liefert alle Filme sortiert wie im DAO definiert
    fun loadAll(): List<Film> = dao.holeAlleFilme()

    fun insert(film: Film) {
        dao.filmEinfuegen(film)
    }

    fun delete(film: Film) {
        dao.loescheFilm(film)
    }
}

