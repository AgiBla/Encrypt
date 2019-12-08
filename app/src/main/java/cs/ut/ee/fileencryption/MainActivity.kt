package cs.ut.ee.fileencryption

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import cs.ut.ee.fileencryption.LocalDbClient.getDatabase

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity-LOG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testFile = FileEntity(
            0, //0 correspond to 'no value', autogenerate handles it for us
            "myTestFile",
            byteArrayOf(1, 0, 1, 1, 0, 1)
        )
        val testFile2 = FileEntity(
            0, //0 correspond to 'no value', autogenerate handles it for us
            "myTestFile2",
            byteArrayOf(0, 1, 1, 1, 0, 0)
        )

        val db = getDatabase(this)
        if (testFile.name !in db!!.getFileDao().loadFilenames()) {
            db.getFileDao().insertFile(testFile)
        }
        for (name in db.getFileDao().loadFilenames()) {
            Log.v(TAG, name)
        }
        // db.clearAllTables()
    }
}