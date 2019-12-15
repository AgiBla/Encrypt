package cs.ut.ee.fileencryption

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cs.ut.ee.fileencryption.LocalDbClient.getDatabase
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    // Url used to generate string of 20 characters containing a-z, A-Z and 0-9
    val url = "https://www.random.org/strings/?num=1&len=20&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new"
    val TAG = "MainActivity-LOG"
    lateinit var FILE_PATH : String
    var ENCRYPT_FOLDER_PATH = "????" //to be determined

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testFile = FileEntity(
            0, //0 correspond to 'no value', autogenerate handles it for us
            "myTestFile",
            byteArrayOf(1, 0, 1, 1, 0, 1)
        )
        val testFile2 = FileEntity(
            0, //0 correspond to 'no value', autogenerate handles it for us
            "myTestFile2",
            byteArrayOf(0, 1, 1, 1, 0, 0)
        )

        val db = getDatabase(this)
        if (testFile.name !in db!!.getFileDao().loadFilenames()) {
            db.getFileDao().insertFile(testFile)
        }
        for (name in db.getFileDao().loadFilenames()) {
            Log.v(TAG, name)
        }
        // db.clearAllTables()

        if(ContextCompat.checkSelfPermission(this , android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            val requestedPermissions: Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ActivityCompat.requestPermissions(this, requestedPermissions, 1)
        }
    }


    fun EncryptFile(view: View) {
        GetRandomString(url)
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        startActivityForResult(intent, requestcode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == -1 && data != null){
            FILE_PATH = data.data!!.path!!
            Log.i("FileEncryption", FILE_PATH)
            val toast = Toast.makeText(applicationContext, data.data!!.path, Toast.LENGTH_SHORT)
            toast.show()
        }else{
            val toast = Toast.makeText(applicationContext, "File not selected", Toast.LENGTH_SHORT)
            toast.show()
        }

        when{// Delete this Log.i and use it to run encrypt and decrypt function
            requestCode == 10 -> Log.i("FileEncryption", "Encrypt button")// Run encryption function and save encrypted file in folder : ENCRYPT_FOLDER_PATH
            requestCode == 20 -> Log.i("FileEncryption", "Decrypt button")// decrypt file in folder ENCRYPT_FOLDER_PATH
        }
    }

    // Reads HTTP request and receives random string from Random.org
    fun GetRandomString(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                //println(response.body()?.string())
                Log.i("patata", response.body()?.string())
            }
        })
    }
}