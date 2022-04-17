package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchActors : AppCompatActivity() {

    private var out: ArrayList<String> = ArrayList<String>()
    lateinit var output: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actors)

        // Initialize the elements
        val actor = findViewById<EditText>(R.id.actorName)
        val searchBtn = findViewById<Button>(R.id.btnSearch)
        output = findViewById<TextView>(R.id.out)

        searchBtn.setOnClickListener {

            val actorName = actor!!.text.toString().trim()  // Get the actor name
            if (actorName.isEmpty()) {
                Toast.makeText(this, "❗Please enter an actor name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            output.text = " "

            val moviesList = mutableListOf<Movie>()

            val db = Room.databaseBuilder(this, AppDatabase::class.java, "movies").build()  // Create the database
            val movieDao = db.movieDao()    // Create the DAO
            runBlocking {
                launch {
                    val movies: List<Movie> = movieDao.getAll()
                    for (movie in movies){
                        if(movie.actors?.contains(actorName,ignoreCase = true) == true){    // Check if the relevant actor is in the movie
                            moviesList.add(movie)
                        }
                    }
                }
            }
            for (i in 0 until moviesList.size){
                output.append(moviesList[i].title + "\n")
                out.add(moviesList[i].title)
            }
        }
    }

    override fun onSaveInstanceState(outState : Bundle) {   //Saving the current data as bundle
        super.onSaveInstanceState(outState)

        outState.putStringArrayList("out", out)
    }

        /**
         * On restore instance state
         *
         * @param savedInstanceState
         */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {   //Getting the saved data(Restoring)
        super.onRestoreInstanceState(savedInstanceState)

        out = savedInstanceState.getStringArrayList("out")!!
            for (i in 0 until out.size){
                output.append(out[i] + "\n")
            }
    }
}