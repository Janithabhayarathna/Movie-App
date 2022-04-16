package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchActors : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actors)

        val actor = findViewById<EditText>(R.id.actorName)
        val searchBtn = findViewById<Button>(R.id.btnSearch)
        val output = findViewById<TextView>(R.id.out)

        searchBtn.setOnClickListener {
            val actorName = actor!!.text.toString().trim()
            output.text = " "

            val moviesList = mutableListOf<Movie>()

            val db = Room.databaseBuilder(this, AppDatabase::class.java,
                "movies").build()
            val movieDao = db.movieDao()
            runBlocking {
                launch {
                    val movies: List<Movie> = movieDao.getAll()
                    for (movie in movies){
                        if(movie.actors?.contains(actorName,ignoreCase = true) == true){
                            moviesList.add(movie)
                        }
                    }
                }
            }
            for (i in 0 until moviesList.size){
                output.append(moviesList[i].title + "\n")
            }
        }
    }
}