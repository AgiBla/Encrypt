package cs.ut.ee.fileencryption

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.rs3vans.krypto.Bytes
import com.github.rs3vans.krypto.Decrypted
import com.github.rs3vans.krypto.Encrypted
import com.github.rs3vans.krypto.hashPassword
import kotlinx.android.synthetic.main.encryptfile.*
import okhttp3.*
import java.io.IOException

class EncryptFile : AppCompatActivity() {

    // Url used to generate string of 20 characters containing a-z, A-Z and 0-9
    private val url = "https://www.random.org/strings/?num=1&len=20&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new"
    private val client = OkHttpClient()
    private var path = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.encryptfile)

        path = intent.getStringExtra("path")

        // Write name of the file in TextView
        val fileName = path.split("/")
        textFileName.text = fileName[fileName.size-1]

        encryptButton.setOnClickListener() {

            if (editText.text.toString().length >= 4) {
                val newPin = editText.text.toString().toInt()

                // First step in encryption. Generate completely random key from Random.org
                getRandomString(newPin)

            } else {
                textError.visibility = View.VISIBLE
            }
        }
    }


    // Reads HTTP request and receives random string from Random.org
    private fun getRandomString(pinCode : Int) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            // When receiving answer from HTTP request
            override fun onResponse(call: Call, response: Response) {
                val key = response.body()?.string() ?: ""


            }
        })
    }
}
