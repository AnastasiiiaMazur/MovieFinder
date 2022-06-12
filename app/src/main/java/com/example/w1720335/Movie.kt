package com.example.w1720335

import androidx.room.Entity
import androidx.room.PrimaryKey

// Creating DB Movie Table dataclass
@Entity
data class Movie (
    @PrimaryKey val id: Int,
    val Title: String,
    val Year: String,
    val Rated: String,
    val Released: String,
    val Runtime: String,
    val Genre: String,
    val Director: String,
    val Writer: String,
    val Actors: String,
    val Plot: String
)

/* Example of the information that should be saved to the DB
Title":"The Shawshank Redemption",
"Year":"1994",
"Rated":"R",
"Released":"14 Oct 1994",
"Runtime":"142 min",
"Genre":"Drama",
"Director":"Frank Darabont",
"Writer":"Stephen King, Frank Darabont",
"Actors":"Tim Robbins, Morgan Freeman, Bob Gunton",
"Plot":"Two imprisoned men bond over a number of years, finding solace
and eventual redemption through acts of common decency." */