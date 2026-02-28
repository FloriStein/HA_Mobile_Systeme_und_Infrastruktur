/**
 * Modul: FilmDao
 * DAO für Tabelle filme Lese Schreibzugriffe Room generiert die Implementierung
 */
package de.hs.harz.kinoapp.data

import androidx.room.*
import de.hs.harz.kinoapp.model.Film

@Dao
interface FilmDao {

    @Query("SELECT COUNT(*) FROM filme")
    fun getAnzahlFilme(): Int

    /** Alle Filme sortiert nach Datum und Uhrzeit */
    @Query("SELECT * FROM filme ORDER BY datum ASC, uhrzeit ASC")
    fun holeAlleFilme(): List<Film>

    @Insert
    fun filmEinfuegen(film: Film)

    @Delete
    fun loescheFilm(film: Film)

    @Query("DELETE FROM filme WHERE id = :id")
    fun loescheMitId(id: Long)
}
