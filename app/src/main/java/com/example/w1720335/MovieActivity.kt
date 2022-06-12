package com.example.w1720335

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MovieActivity : AppCompatActivity() {

    var retrieve_button: Button? = null
    var add_button: Button? = null
    var mtv: TextView? = null
    var edt: EditText? = null
    var url_string: String? = null
    var MY_API_KEY: String? = null
    var current_json: JSONObject? = null
    var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        //connection to the db
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mydatabase").build()
        val movieDao = db.movieDao()

        retrieve_button = findViewById(R.id.retrieve_button)
        add_button = findViewById(R.id.add_button)
        mtv = findViewById(R.id.mtv)
        edt = findViewById(R.id.edt)

        MY_API_KEY = resources.getString(R.string.MY_API_KEY)

        // counting how many rows has DB using SQL query, to fill the ID value in the table with correct number
        runBlocking {
            launch {
                count = movieDao.countMovies()
            }
        }

        retrieve_button?.setOnClickListener {
            getMovie()
        }

        add_button?.setOnClickListener {
            runBlocking {
                launch {
                    var movie = add_movie(current_json)
                    movieDao.insertMovie(movie)
                }
            }
        }
    }

    fun getMovie() {
        var movie_name = edt!!.text.toString().trim()

        // "https://www.omdbapi.com/?t=$movie_name&apikey=$MY_API_KEY"
        // "https://www.omdbapi.com/?t=" + movie_name + "&apikey=" +MY_API_KEY
        url_string = "https://www.omdbapi.com/?t=$movie_name&apikey=$MY_API_KEY"

        var data: String = ""

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

            mtv?.setText(data)
        }
    }

    //extracts the relevant info from the whole JSON string returned by the web service
    suspend fun parseJSON(stb: StringBuilder): String {
        val json = JSONObject(stb.toString())
        current_json = json

        var title = json["Title"] as String
        var year = json["Year"] as String
        var rated = json["Rated"] as String
        var released = json["Released"] as String
        var runtime = json["Runtime"] as String
        var genre = json["Genre"] as String
        var director = json["Director"] as String
        var writer = json["Writer"] as String
        var actors = json["Actors"] as String
        var plot = json["Plot"] as String

        var info = "Title: " + title +
                "\nYear: " + year +
                "\nRated: " + rated +
                "\nReleased: " + released +
                "\nRuntime: " + runtime +
                "\nGenre: " + genre +
                "\nDirector: " + director +
                "\nWriter: " + writer +
                "\nActors: " + actors +
                "\nPlot: " + plot

        return info
    }

    // function to add retrieved movie to the local Room library
    fun add_movie(current_json: JSONObject?): Movie {
        var title = current_json?.get("Title") as String
        var year = current_json["Year"] as String
        var rated = current_json["Rated"] as String
        var released = current_json["Released"] as String
        var runtime = current_json["Runtime"] as String
        var genre = current_json["Genre"] as String
        var director = current_json["Director"] as String
        var writer = current_json["Writer"] as String
        var actors = current_json["Actors"] as String
        var plot = current_json["Plot"] as String

        count++
        val movie = Movie(count, title, year, rated, released, runtime, genre, director, writer, actors, plot)

        return movie
    }

    // trying to implement saving information while rotating the screen
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("counter", count)
        outState.putString("info", mtv?.getText().toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mtv?.setText(savedInstanceState.getString("info"))
        count = savedInstanceState.getInt("counter")
    }
}


