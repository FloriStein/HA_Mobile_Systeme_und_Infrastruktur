/**
 * Modul: Film (Datenmodell)
 * Datenmodell der App gleichzeitig Room-Entity Tabelle filme in SQLite
 *
 * Alle Basisinfos zu einem Film Datum Uhrzeit Titel Genre Saal Beschreibung
 * bildUri ist optional wenn gesetzt zeigt die App ein Bild sonst ein Icon
 * Room speichert die URI als String weil es keinen Uri-Typ unterstützt
 */
package de.hs.harz.kinoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// data class automatisch equals hashCode toString ideal für Entities
@Entity(tableName = "filme")
data class Film(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val datum: String,
    val uhrzeit: String,
    val titel: String,
    val beschreibung: String,
    val genre: String,
    val saal: String,
    // Optional URI zum Bild als String gespeichert Room kennt keinen Uri-Typ
    val bildUri: String? = null
)
