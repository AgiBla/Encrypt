package cs.ut.ee.fileencryption

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.file_list.*


class FileList : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_list)

        // Read file names from database
        val db = LocalDbClient.getDatabase(this)
        var listAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, db!!.getFileDao().loadFilenames())
        files_listview.adapter = listAdapter

        files_listview.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val name = listAdapter.getItem(position)!!

            // Create new activity to decrypt selected file
            val intent = Intent(this, DecryptFile()::class.java)
            intent.putExtra("name", name)
            startActivityForResult(intent, 1)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        finish()
    }
}