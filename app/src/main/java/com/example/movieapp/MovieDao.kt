package com.example.movieapp

import androidx.room.*

interface MovieDao {

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg user: Movie)
    @Insert
    suspend fun insertAll(vararg users: Movie)

}