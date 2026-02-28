/**
 * DAO (Data Access Object) für Room – Schnittstelle zur Tabelle "filme".
 *
 * Enthält alle DB-Operationen: lesen, einfügen, löschen. Room implementiert
 * die Methoden zur Compile-Zeit – man schreibt nur die Annotationen.
 */
package de.hs.harz.kinoapp.data

import androidx.room.*
import de.hs.harz.kinoapp.model.Film

@Dao
interface FilmDao {

    @Query("SELECT COUNT(*) FROM filme")
    fun getAnzahlFilme(): Int

    // Sortiert nach Datum und Uhrzeit. Achtung: Da beides als String gespeichert ist,
    // funktioniert das nur mit konsequentem Format (TT.MM.JJJJ, HH:MM). Für echte
    // Robustheit wären ISO-Datum oder Long-Timestamps besser.
    @Query("SELECT * FROM filme ORDER BY datum ASC, uhrzeit ASC")
    fun holeAlleFilme(): List<Film>

    @Insert
    fun filmEinfuegen(film: Film)

    @Delete
    fun loescheFilm(film: Film)

    @Query("DELETE FROM filme WHERE id = :id")
    fun loescheMitId(id: Long)
}
