package com.example.movieapp

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movie: Movie)
    @Insert
    suspend fun insertAll(vararg movie: Movie)

}