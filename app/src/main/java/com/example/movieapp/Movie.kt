package com.example.movieapp
import androidx.room.*

@Entity
data class Movie(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val year: String?,
    val rated: String?,
    val released: String?,
    val runTime: String?,
    val genre: String?,
    val director: String?,
    val writer: String?,
    val actors: String?,
    val plot: String?,
)
