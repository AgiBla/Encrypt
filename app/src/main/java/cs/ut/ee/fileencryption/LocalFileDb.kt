package cs.ut.ee.fileencryption

import androidx.room.Database
import androidx.room.RoomDatabase

@Database (entities = arrayOf (FileEntity::class), version = 2)
abstract class LocalFileDb : RoomDatabase() {
    abstract fun getFileDao(): FileDao
}