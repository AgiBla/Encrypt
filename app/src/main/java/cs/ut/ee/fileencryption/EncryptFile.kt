package cs.ut.ee.fileencryption

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.encryptfile.*

class EncryptFile : AppCompatActivity() {

    // Url used to generate string of 20 characters containing a-z, A-Z and 0-9
    val url = "https://www.random.org/strings/?num=1&len=20&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.encryptfile)

        val path = intent.getStringExtra("path")

        // Write name of the file in TextView
        val fileName = path.split("/")
        textFileName.text = fileName[fileName.size-1]

        encryptButton.setOnClickListener() {

            if (editText.text.toString().length >= 4) {
                val newPin = editText.text.toString().toInt()

                finish()
            } else {
                textError.visibility = View.VISIBLE
            }
        }
    }
}
