package cs.ut.ee.fileencryption

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.rs3vans.krypto.*
import kotlinx.android.synthetic.main.decryptfile.*
import kotlinx.android.synthetic.main.encryptfile.editText
import kotlinx.android.synthetic.main.encryptfile.encryptButton
import kotlinx.android.synthetic.main.encryptfile.pBar
import kotlinx.android.synthetic.main.encryptfile.textError
import kotlinx.android.synthetic.main.encryptfile.textError2
import kotlinx.android.synthetic.main.encryptfile.textFileName
import java.io.File
import javax.crypto.spec.SecretKeySpec


class DecryptFile : AppCompatActivity() {

    private var content = listOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.decryptfile)

        var name = ""
        var path = ""

        // Activity launched from app
        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name") ?: ""
            path = intent.getStringExtra("path") ?: ""

        // Activity launched from file explorer (opening .crypt file)
        } else {
            name = intent.data!!.lastPathSegment.toString()
            path = intent.data!!.path ?: ""

            // Some file explorers may include this
            path = path.replace("/root", "")
        }

        // Read file information from database
        if (path == "") {
            val db = LocalDbClient.getDatabase(this)
            val file = db!!.getFileDao().findByName(name)
            path = file.contentByte

            // Remove file entry from database
            deleteButton.setOnClickListener {
                db.getFileDao().deleteFile(file)
                finish()
            }
        }

        // Display path
        Toast.makeText(applicationContext, path, Toast.LENGTH_LONG).show()

        // Try to read from file. If exception file doesn't exist
        try {
            content = String(File(path).readBytes(), Charsets.ISO_8859_1).split("#####")
            deleteButton.visibility = View.INVISIBLE
        } catch (e : Exception) {
            textError2.visibility = View.VISIBLE
            encryptButton.isEnabled = false
        }

        // Write name of the file in TextView
        textFileName.text = name

        encryptButton.setOnClickListener {

            val len = editText.text.toString().length
            // Inserted pin is at least 4 numbers
            if (len in 4..16) {

                // Create decryption key combining pin and salt
                val key = editText.text.toString() + "0".repeat(16-len)

                // Create cipher with generated key and AES algorithm used for encryption
                val cipher = BlockCipher(SecretKeySpec(key.toByteArray(), "AES"))

                try {

                    // Decrypt check. If equals name then pin is correct
                    val checkByte = Bytes(content[3].toByteArray(Charsets.ISO_8859_1))
                    val checkInit = Bytes(content[4].toByteArray(Charsets.ISO_8859_1))

                    val check = cipher.decrypt(Encrypted(checkByte, checkInit))

                    if (check.bytes.toDecodedString() == content[0] /*Name*/){

                        // Recover secret key and create new cipher
                        val keyByte = Bytes(content[1].toByteArray(Charsets.ISO_8859_1))
                        val keyInit = Bytes(content[2].toByteArray(Charsets.ISO_8859_1))

                        val decryptedKey = cipher.decrypt(Encrypted(keyByte, keyInit))
                        val secretCipher = BlockCipher(importAesKey(decryptedKey.bytes))

                        // Show animation decrypting
                        pBar.visibility = View.VISIBLE
                        textError.visibility = View.INVISIBLE

                        // Decrypt content
                        val contentByte = Bytes(content[5].toByteArray(Charsets.ISO_8859_1))
                        val contentInit = Bytes(content[6].toByteArray(Charsets.ISO_8859_1))

                        val content = secretCipher.decrypt(Encrypted(contentByte, contentInit))

                        // Write new file
                        val f = File(path.replace(".crypt", ""))
                        f.writeBytes(content.bytes.byteArray)

                        finish()

                    } else {
                        textError.visibility = View.VISIBLE
                    }

                } catch (e : java.lang.Exception) {
                    textError.visibility = View.VISIBLE
                }
            } else {
                textError.visibility = View.VISIBLE
            }
        }
    }
}
