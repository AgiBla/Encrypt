package cs.ut.ee.fileencryption

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class DecryptFile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.decryptfile)

        /*

        var uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        var cursor = contentResolver.query(uri, SELECTED_COLUMS, null, null, null)

        if(cursor != null  ){

            val toViews = intArrayOf(R.id.name_textview)//, R.id.number_textview)

            cursorAdapter = SimpleCursorAdapter(
                    this,
                            R.layout.list_row,
                            cursor,
                            SELECTED_COLUMS,
                            toViews,
                            0)
            main_listview.setAdapter(cursorAdapter)
        }
         */
    }

    fun DecriptSelectedFile(view: View) {

        //OnClick the file name
    }
}