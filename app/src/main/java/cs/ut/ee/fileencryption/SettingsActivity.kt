package cs.ut.ee.fileencryption

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        encryptButton.setOnClickListener() {
            val db = LocalDbClient.getDatabase(this)
            db?.clearAllTables()

            finish()
        }
    }
}