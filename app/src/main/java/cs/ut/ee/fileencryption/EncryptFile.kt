package cs.ut.ee.fileencryption

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.rs3vans.krypto.BlockCipher
import com.github.rs3vans.krypto.Bytes
import com.github.rs3vans.krypto.Decrypted
import com.github.rs3vans.krypto.toBytes
import cs.ut.ee.fileencryption.LocalDbClient.getDatabase
import kotlinx.android.synthetic.main.encryptfile.*
import okhttp3.*
import java.io.File
import java.io.IOException
import javax.crypto.spec.SecretKeySpec


class EncryptFile : AppCompatActivity() {

    private val client = OkHttpClient()
    private var path = ""
    private var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.encryptfile)

        path = intent.getStringExtra("path")
        name = intent.getStringExtra("name")

        try {
            File(path).readBytes()
        } catch (e : Exception) {
            textError2.visibility = View.VISIBLE
            encryptButton.isEnabled = false
        }

        // Write name of the file in TextView
        textFileName.text = name

        encryptButton.setOnClickListener() {

            if (editText.text.toString().length >= 4) {
                val newPin = editText.text.toString().toInt()

                // Hide previous error message
                textError.visibility = View.INVISIBLE

                // Make loading circle animation play
                pBar.visibility = View.VISIBLE

                // First step in encryption. Generate completely random key from Random.org
                getRandomString(newPin)

            } else {
                textError.visibility = View.VISIBLE
            }
        }
    }


    // Reads HTTP request and receives random string from Random.org
    private fun getRandomString(pinCode : Int) {

        // Url used to generate string of 16 characters containing a-z, A-Z and 0-9
        val len = 15-pinCode.toString().length
        val url = "https://www.random.org/strings/?num=1&len=$len&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {

                // Show error message
                runOnUiThread {
                    Toast.makeText(applicationContext, R.string.toast_4, Toast.LENGTH_SHORT).show()
                    pBar.visibility = View.INVISIBLE
                }
            }

            // When receiving answer from HTTP request
            override fun onResponse(call: Call, response: Response) {
                val salt = response.body()?.string() ?: ""
                val key = pinCode.toString() + salt

                createCipher(key, salt)
            }
        })
    }

    private fun createCipher(key : String, salt : String){

        // Create cipher with generated key and AES algorithm used for encryption
        val cipher = BlockCipher(SecretKeySpec(key.toByteArray(), "AES"))

        // Read content of the file
        val file = File(path)
        val bytes = file.readBytes()

        // Encrypt file content and its name (which will be used to check if pin is correct)
        val check = Decrypted(name.toBytes())
        val content = Decrypted(Bytes(bytes))

        val encryptedCheck = cipher.encrypt(check)
        val encryptedContent = cipher.encrypt(content)

        try {
            var f = File(path + ".crypt")
            f.writeBytes(encryptedContent.bytes.byteArray)
        } catch (e: java.lang.Exception) {
            runOnUiThread {
                textError2.visibility = View.VISIBLE
                pBar.visibility = View.INVISIBLE
            }
            finish()
        }

        // Create object to store in database
        val encrytedFile = FileEntity(
            0, //0 correspond to 'no value', autogenerate handles it for us
            name,
            salt,
            encryptedCheck.bytes.byteArray,
            encryptedCheck.initVector?.byteArray!!,
            path+".crypt",
            encryptedContent.initVector?.byteArray!!
        )

        // Store object in database
        val db = getDatabase(this)
        db?.getFileDao()?.insertFile(encrytedFile)

        setResult(10)
        finish()
    }
}
