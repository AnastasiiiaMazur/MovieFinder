package com.example.w1720335

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


// Activity to find movies from Room library by string of the actor's name
class ActorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor)

        var button = findViewById<Button>(R.id.button)
        var edt = findViewById<EditText>(R.id.edt)
        var tv = findViewById<TextView>(R.id.tv)

        val db = Room.databaseBuilder(this, AppDatabase::class.java,
            "mydatabase").build()
        val movieDao = db.movieDao()

        var a: String
        button.setOnClickListener {
            runBlocking {
                launch {
                    val allActors = movieDao.getAll()

                    a = edt.text.toString().trim()

                    tv.setText("")

                    for (i in allActors) {
                        if (i!!.Actors!!.contains(a, true))
                            tv.append(i.Title + "\n")
                    }
                }
            }
        }
    }
}
