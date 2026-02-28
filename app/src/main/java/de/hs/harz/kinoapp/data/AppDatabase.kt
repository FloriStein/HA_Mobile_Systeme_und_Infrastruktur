/**
 * Modul: AppDatabase (KinoDatabase)
 * Room-Datenbank Singleton allowMainThreadQueries
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
        // Singleton verhindert mehrere DB-Verbindungen
        private var instance: KinoDatabase? = null

        fun getDatabase(context: Context): KinoDatabase {
            // Keine Instanz vorhanden neue DB bauen
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    KinoDatabase::class.java,
                    "filme_db"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}
