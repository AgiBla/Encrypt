package cs.ut.ee.fileencryption

import android.Manifest.*
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.MenuItem






class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        if(ContextCompat.checkSelfPermission(this , permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val requestedPermissions: Array<String> = arrayOf(permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, requestedPermissions, 1)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity()::class.java))
        }
        return true
    }


    fun encryptFile(view: View) {
        openFileExplorer(10)
    }

    fun decryptFile(view: View) {
        val intent = Intent(this, FileList::class.java)
        startActivity(intent)
    }

    fun openFileExplorer(requestCode: Int){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result code correctly encrypted file
        if (resultCode == 10){
            Toast.makeText(applicationContext, R.string.toast_2, Toast.LENGTH_SHORT).show()
        }

        // File selected
        else if (resultCode == -1 && data != null) {
            val uri = data.data

            var name = ""
            var path = ""

            try {
                // This should try to get the absolute path. Seem to only work on older android versions
                // Code from: https://stackoverflow.com/questions/3401579
                if (uri!!.scheme.toString().compareTo("content") == 0) {
                    val cursor: Cursor = contentResolver.query(uri, null, null, null, null)!!
                    if (cursor.moveToFirst()) {
                        val columnIndex: Int =
                            cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) //Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                        val filePathUri =
                            Uri.parse(cursor.getString(columnIndex))
                        name = filePathUri.lastPathSegment.toString()
                        path = filePathUri.path!!
                    }
                }
            } catch (e : Exception) {
                path = uri!!.path!!.split(":")[1]

                val fileName = path.split("/")
                name = fileName[fileName.size-1]
            }

            // Decrypt file
            if (path.endsWith(".crypt")) {
                val intent = Intent(this, DecryptFile()::class.java)
                intent.putExtra("path", path)
                intent.putExtra("name", name.replace(".crypt", ""))
                startActivityForResult(intent, 1)

            // Encrypt file
            } else {
                val intent = Intent(this, EncryptFile()::class.java)
                intent.putExtra("path", path)
                intent.putExtra("name", name)
                startActivityForResult(intent, 1)
            }
        }
    }
}