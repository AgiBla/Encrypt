package cs.ut.ee.fileencryption

import android.content.Context
import androidx.room.Room

object LocalDbClient {
    var fileDb : LocalFileDb? = null
    fun getDatabase(context: Context) : LocalFileDb? {

        if (fileDb == null){
            fileDb = Room.databaseBuilder(
                context, LocalFileDb::class.java, "myFiles")
                .fallbackToDestructiveMigration() // each time schema changes, data is lost!
                .allowMainThreadQueries() // if possible, use background thread instead
                .build()
        }
        return fileDb
    }

}