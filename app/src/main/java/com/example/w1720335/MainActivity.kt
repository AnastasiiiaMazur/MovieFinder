package com.example.w1720335

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/* During implementation of this program had a problem with emulator and connection to the Web Service. To fix this, have to
* Cold Boot emulator before running the program. */
class MainActivity : AppCompatActivity() {

    var add_to_db: Button? = null
    var search_movies: Button? = null
    var search_actors: Button? = null
    var movie_web: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // connection to the db
        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mydatabase").build()
        val movieDao = db.movieDao()

        search_movies = findViewById(R.id.search_movies)
        search_actors = findViewById(R.id.search_actors)
        add_to_db = findViewById(R.id.add_to_db)
        movie_web = findViewById(R.id.search_web)

        // hardcoding given movies, so when user clicks on "Save to DB" button, they will be added to Room library
        val m1 = Movie(1,"The Shawshank Redemption","1994","R","14 Oct 1994", "142 min","Drama",
            "Frank Darabont","Stephen King, Frank Darabont","Tim Robbins, Morgan Freeman, Bob Gunton",
            "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.")

        val m2 = Movie(2, "Batman: The Dark Knight Returns, Part 1","2012","PG-13","25 Sep 2012","76 min","Animation, Action, Crime, Drama, Thriller",
            "Jay Oliva","Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman","Peter Weller, Ariel Winter, David Selby, Wade Williams",
            "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?")

        val m3 = Movie(3,"The Lord of the Rings: The Return of the King","2003","PG-13","17 Dec 2003","201 min","Action, Adventure, Drama",
            "Peter Jackson","J.R.R. Tolkien, Fran Walsh, Philippa Boyens","Elijah Wood, Viggo Mortensen, Ian McKellen",
            "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.")

        val m4 = Movie(4,"Inception","2010","PG-13","16 Jul 2010","148 min","Action, Adventure, Sci-Fi",
            "Christopher Nolan", "Christopher Nolan","Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
            "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.")

        val m5 = Movie(5,"The Matrix","1999","R","31 Mar 1999","136 min","Action, Sci-Fi",
            "Lana Wachowski, Lilly Wachowski","Lilly Wachowski, Lana Wachowski","Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
            "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.")


        add_to_db?.setOnClickListener {
            runBlocking {
                launch {
                    movieDao.insertMovie(m1,m2,m3,m4,m5)
                }
            }
        }

        search_movies?.setOnClickListener {
            val i = Intent(this, MovieActivity::class.java)
            startActivity(i)
        }

        search_actors?.setOnClickListener {
            val i = Intent(this, ActorActivity::class.java)
            startActivity(i)
        }

        movie_web?.setOnClickListener {
            val i = Intent(this, WebServiceMovieActivity::class.java)
            startActivity(i)
        }
    }
}