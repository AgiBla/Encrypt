package cs.ut.ee.fileencryption

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.rs3vans.krypto.*
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

                encrypt(newPin)

            } else {
                textError.visibility = View.VISIBLE
            }
        }
    }


    // Reads HTTP request and receives random string from Random.org
    private fun encrypt(pinCode : Int) {

        // Generate first key used to encrypt the more secure key and its cipher
        val len = 16-pinCode.toString().length
        val key = pinCode.toString() + "0".repeat(len)

        val cipher = BlockCipher(SecretKeySpec(key.toByteArray(), "AES"))


        // Generate second key used to encrypt and its cipher
        val secretKey = generateRandomAesKey()
        val secretCipher = BlockCipher(secretKey)

        // Encrypt secret key with first cipher
        val encryptedKey = cipher.encrypt(Decrypted(secretKey.toBytes()))


        // Read content of the file
        val file = File(path)
        val bytes = file.readBytes()

        // Encrypt file content and its name (using different ciphers)
        val check = Decrypted(name.toBytes())
        val content = Decrypted(Bytes(bytes))

        val encryptedCheck = cipher.encrypt(check)
        val encryptedContent = secretCipher.encrypt(content)

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
            encryptedKey.bytes.byteArray,
            encryptedKey.initVector?.byteArray!!,
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
