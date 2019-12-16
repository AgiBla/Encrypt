package cs.ut.ee.fileencryption

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file")
data class FileEntity (
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name : String?,
    var key : String?,
    var checkByte : ByteArray?,
    var checkInit : ByteArray?,
    var contentByte : ByteArray?,
    var contentInit : ByteArray?
)