package cs.ut.ee.fileencryption

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.rs3vans.krypto.*
import kotlinx.android.synthetic.main.decryptfile.*
import kotlinx.android.synthetic.main.encryptfile.*
import kotlinx.android.synthetic.main.encryptfile.editText
import kotlinx.android.synthetic.main.encryptfile.encryptButton
import kotlinx.android.synthetic.main.encryptfile.pBar
import kotlinx.android.synthetic.main.encryptfile.textError
import kotlinx.android.synthetic.main.encryptfile.textError2
import kotlinx.android.synthetic.main.encryptfile.textFileName
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

        // Try to read from file. If exception file doesn't exist
        try {
            content = File(file.contentByte).readBytes()
            deleteButton.visibility = View.INVISIBLE
        } catch (e : Exception) {
            textError2.visibility = View.VISIBLE
            encryptButton.isEnabled = false
        }

        // Write name of the file in TextView
        textFileName.text = file.name

        encryptButton.setOnClickListener() {

            val len = editText.text.toString().length
            // Inserted pin is at least 4 numbers
            if (len >= 4) {

                // Create decryption key combining pin and salt
                val key = editText.text.toString() + "0".repeat(16-len)
                Log.i("patata", key)

                // Create cipher with generated key and AES algorithm used for encryption
                val cipher = BlockCipher(SecretKeySpec(key.toByteArray(), "AES"))

            //    try {

                    // Decrypt check. If equals name then pin is correct
                    var check = cipher.decrypt(Encrypted(Bytes(file.checkByte), Bytes(file.checkInit)))
                    if (check.bytes.toDecodedString() == name){

                        // Recover secret key and create new cipher
                        val decryptedKey = cipher.decrypt(Encrypted(Bytes(file.key), Bytes(file.keyInit)))
                        val secretCipher = BlockCipher(importAesKey(decryptedKey.bytes))

                        // Show animation decrypting
                        pBar.visibility = View.VISIBLE
                        textError.visibility = View.INVISIBLE

                        // Read content from stored file
                        val bytes = File(file.contentByte).readBytes()

                        // Decrypt content
                        var content = secretCipher.decrypt(Encrypted(Bytes(bytes), Bytes(file.contentInit)))

                        // Write new file
                        var f = File(file.contentByte.replace(".crypt", ""))
                        f.writeBytes(content.bytes.byteArray)

                        finish()

                    } else {
                        textError.visibility = View.VISIBLE
                    }

       //         } catch (e : java.lang.Exception) {
        //            textError.visibility = View.VISIBLE
        //        }
            } else {
                textError.visibility = View.VISIBLE
            }
        }

        // Remove file entry from database
        deleteButton.setOnClickListener() {
            db.getFileDao().deleteFile(file)
            finish()
        }
    }
}
