package com.example.w1720335

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

// Activity to find all movies containing string that was typed by the user from Web Service
class WebServiceMovieActivity : AppCompatActivity() {
    var search_button: Button? = null
    var wtv: TextView? = null
    var edt: EditText? = null
    var url_string: String? = null
    var MY_API_KEY: String? = null

    var data = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_service_movie)

        search_button = findViewById(R.id.search_button)
        wtv = findViewById<TextView>(R.id.wtv)
        edt = findViewById(R.id.edt)

        MY_API_KEY = resources.getString(R.string.MY_API_KEY)

        search_button?.setOnClickListener {
            wtv?.setText("")
            getMovies()
        }
    }

    fun getMovies() {
        var movie_string = edt!!.text.toString().trim()

        /*
          The link on line 52 (commented out) is to search for movies with specific movie string
          The link on line 53 is a regex expression for movies with the string
        */
//        url_string = "https://www.omdbapi.com/?s=$movie_string&apikey=$MY_API_KEY"
        url_string = "https://www.omdbapi.com/?s=*$movie_string*&apikey=$MY_API_KEY"
        /*
            The API returns the results for the first page only to view other pages query param "page" have to be specified
            for this app a user will only see results of first page, if unsatisfied with the return result he will need to be more
            specific with his input.
         */
        runBlocking {
            withContext(Dispatchers.IO) {

                //collect all JSON from web service as string
                var stb = StringBuilder("")

                var url = URL(url_string)
                var con = url.openConnection() as HttpURLConnection

                // use to read line by line
                val bf: BufferedReader
                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream))
                }
                catch (e: IOException) {
                    e.printStackTrace()
                    return@withContext
                }

                var line = bf.readLine()
                while (line != null) {
                    stb.append(line)
                    line = bf.readLine()
                }

                data = parseJSON(stb)
            }
            var sz = data.size
            for (i in 0..sz - 1)
                wtv?.append(data[i] + "\n")
        }
    }

    //extracts the relevant info from the whole JSON string returned by the web service
    suspend fun parseJSON(stb: StringBuilder): ArrayList<String> {
        var title: String? = null
        var json_search: JSONObject? = null
        var info = ArrayList<String>()

        val json = JSONObject(stb.toString())
        val jarray = json.getJSONArray("Search")
        for (i in 0..jarray.length()-1) {
            json_search = jarray.getJSONObject(i)
            title = json_search?.get("Title") as String
            info.add(title)
        }
        return info
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList("data", data)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        data = savedInstanceState.getStringArrayList("data") as ArrayList<String>
        //data = savedInstanceState.getString("data").toMutableList().joinToString()
    }
}