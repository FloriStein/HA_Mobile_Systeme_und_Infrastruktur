/**
 * Datenbank-Schicht (Room) – zentrale DB der App.
 *
 * Absichtlich simpel gehalten: Singleton (eine Instanz pro Prozess) und
 * Main-Thread-Zugriff via allowMainThreadQueries(). Für eine Hausarbeit okay,
 * in Produktion würde man Coroutines/Flow verwenden.
 */
package de.hs.harz.kinoapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.hs.harz.kinoapp.model.Film

@Database(entities = [Film::class], version = 1, exportSchema = false)
abstract class KinoDatabase : RoomDatabase() {

    // Einziger Zugriffspunkt auf Film-Operationen
    abstract fun filmDao(): FilmDao

    companion object {
        // Singleton – verhindert mehrere DB-Verbindungen
        private var instance: KinoDatabase? = null

        fun getDatabase(context: Context): KinoDatabase {
            // Keine Instanz vorhanden – neue DB bauen
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    KinoDatabase::class.java,
                    "filme_db"
                )
                    // Zugriffe im UI-Thread – für die Hausarbeit ausreichend
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}
