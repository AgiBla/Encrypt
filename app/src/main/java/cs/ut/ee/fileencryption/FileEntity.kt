package cs.ut.ee.fileencryption

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class FileEntity (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name : String,
    var contentByte : String
)