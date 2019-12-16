package cs.ut.ee.fileencryption

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.rs3vans.krypto.*
import kotlinx.android.synthetic.main.encryptfile.*
import java.io.File
import javax.crypto.spec.SecretKeySpec


class DecryptFile : AppCompatActivity() {

    var content = ByteArray(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.decryptfile)

        var name = intent.getStringExtra("name")

        val db = LocalDbClient.getDatabase(this)
        val file = db!!.getFileDao().findByName(name)

        try {
            content = File(file.contentByte).readBytes()
        } catch (e : Exception) {
            textError2.visibility = View.VISIBLE
            encryptButton.isEnabled = false
        }

        // Write name of the file in TextView
        textFileName.text = file.name

        encryptButton.setOnClickListener() {

            // Inserted pin is at least 4 numbers
            if (editText.text.toString().length >= 4) {

                // Create decryption key combining pin and salt
                val key = editText.text.toString().toString() + file.key

                // Create cipher with generated key and AES algorithm used for encryption
                val cipher = BlockCipher(SecretKeySpec(key.toByteArray(), "AES"))

                try {

                    // Decrypt check. If equals name then pin is correct
                    var check = cipher.decrypt(Encrypted(Bytes(file.checkByte), Bytes(file.checkInit)))
                    if (check.bytes.toDecodedString() == name){

                        // Show animation decrypting
                        pBar.visibility = View.VISIBLE
                        textError.visibility = View.INVISIBLE

                        // Read content from stored file
                        val bytes = File(file.contentByte).readBytes()

                        // Decrypt content
                        var content = cipher.decrypt(Encrypted(Bytes(bytes), Bytes(file.contentInit)))

                        // Write new file
                        var f = File(file.contentByte.replace(".crypt", ""))
                        f.writeBytes(content.bytes.byteArray)

                        finish()

                    } else {
                        textError.visibility = View.VISIBLE
                    }

                } catch (e : java.lang.Exception) {
                    textError.visibility = View.VISIBLE
                }
            }
        }
    }
}
