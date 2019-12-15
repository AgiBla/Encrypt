package cs.ut.ee.fileencryption

import android.util.Log
import java.net.URL

class GetRandom  {

    public fun GetRandom() {
        // Getting URL to the Google Directions API
        val url = "https://www.random.org/strings/?num=1&len=25&digits=on&upperalpha=on&loweralpha=on&unique=on&format=plain&rnd=new"

        val result = URL(url).readText()

        Log.i("patata", result)
    }
}