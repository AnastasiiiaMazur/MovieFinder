package com.example.w1720335

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface MovieDao {

    @Query("Select * from Movie")
    suspend fun getAll(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(vararg movie: Movie)

    @Insert
    suspend fun insertAll(vararg movies: Movie)

    @Query("SELECT COUNT(id) FROM Movie")
    suspend fun countMovies(): Int
}