package cs.ut.ee.fileencryption

import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.rs3vans.krypto.*
import cs.ut.ee.fileencryption.LocalDbClient.getDatabase
import javax.crypto.spec.SecretKeySpec


class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity-LOG"
    lateinit var FILE_PATH : String
    var ENCRYPT_FOLDER_PATH = "????" //to be determined

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        var test = Decrypted("test".toBytes())


        var cipher = BlockCipher(SecretKeySpec("1234567890123456".toByteArray(), "AES"))
        var enc = cipher.encrypt(test)

        var result = cipher.decrypt(Encrypted(Bytes(enc.bytes.byteArray), Bytes(enc.initVector!!.byteArray)))


        Log.i("patata", result.bytes.toDecodedString())



        val db = getDatabase(this)

        for (name in db!!.getFileDao().loadFilenames()) {
            Log.i("patata", name)
        }



        if(ContextCompat.checkSelfPermission(this , android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val requestedPermissions: Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, requestedPermissions, 1)
        }
    }


    fun EncryptFile(view: View) {
        OpenFileExplorer(10)
    }

    fun DecryptFile(view: View) {

        OpenFileExplorer(20)
        /*
        val selectedUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + "/Pictures/")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(selectedUri, "resource/folder")
        startActivityForResult(intent, 20)
        */
    }

    fun OpenFileExplorer(requestcode: Int){

        val toast = Toast.makeText(applicationContext, "Select file you want to encrypt", Toast.LENGTH_LONG)
        toast.show()

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        startActivityForResult(intent, requestcode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result code correctly encrypted file
        if (resultCode == 10){
            Toast.makeText(applicationContext, "File encrypted successfully", Toast.LENGTH_SHORT).show()
        }

        // File selected
        else if (resultCode == -1 && data != null){
            val uri = data.data

            // Code from: https://stackoverflow.com/questions/3401579
            if (uri!!.scheme.toString().compareTo("content") == 0) {
                val cursor: Cursor = contentResolver.query(uri, null, null, null, null)!!
                if (cursor.moveToFirst()) {
                    val column_index: Int =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) //Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                    val filePathUri =
                        Uri.parse(cursor.getString(column_index))
                    val file_name = filePathUri.lastPathSegment.toString()
                    val file_path = filePathUri.path

                    Toast.makeText(applicationContext, file_path, Toast.LENGTH_SHORT).show()

                    // Create new activity to encrypt selected file
                    val intent = Intent(this, EncryptFile()::class.java)
                    intent.putExtra("path", file_path)
                    intent.putExtra("name", file_name)
                    startActivityForResult(intent, 1)
                }
            }
        }

        // No file selected
        else {
            Toast.makeText(applicationContext, "File not selected", Toast.LENGTH_SHORT).show()
        }
    }
}