package com.example.movieapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovies : AppCompatActivity() {
    lateinit var title: String
    lateinit var year: String
    lateinit var rated: String
    lateinit var released: String
    lateinit var runtime: String
    lateinit var genre: String
    lateinit var director: String
    lateinit var writer: String
    lateinit var actors: String
    lateinit var plot: String
    private var info = " "
    private lateinit var textOut: TextView
    private var searchMovieClicked = false
    var saveMovieClicked = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        // Initialize the elements
        val movie = findViewById<EditText>(R.id.movieName)
        textOut = findViewById(R.id.movieText)
        val btn = findViewById<Button>(R.id.retreiveBtn)
        val btnSave = findViewById<Button>(R.id.saveMovieBtn)


        btn?.setOnClickListener {

            searchMovieClicked = true
            val movieName = movie!!.text.toString().trim()  // Get the movie name
            if (movieName.isEmpty()) {
                searchMovieClicked = false
                Toast.makeText(this, "❗Please enter a movie name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val urlString = "https://www.omdbapi.com/?t=$movieName&apikey=ff95b66" // URL to get the movie details

            var data = ""

            runBlocking {
                    withContext(Dispatchers.IO) {
                        val stb = StringBuilder("")

                        val url = URL(urlString)    // Create the URL
                        val con = url.openConnection() as HttpURLConnection // Open the connection
                        val bf: BufferedReader  // Create the buffer reader
                        try {
                            bf = BufferedReader(InputStreamReader(con.inputStream)) // Read the data
                        } catch (e: IOException) {
                            e.printStackTrace()
                            return@withContext
                        }
                        var line = bf.readLine()    // Read the data
                        while (line != null) {
                            stb.append(line)
                            line = bf.readLine()
                        }

                        data = parseJSON(stb)    // Parse the data
                    }
            }
            textOut.text = data
        }

        btnSave?.setOnClickListener {

            saveMovieClicked = true
            if (searchMovieClicked) {
                val db = Room.databaseBuilder(this, AppDatabase::class.java, "movies")
                    .build()  // Create the database
                val movieDao = db.movieDao() // Create the DAO

                runBlocking {
                    launch {
                        val insertMovie = Movie(
                            title,
                            year,
                            rated,
                            released,
                            runtime,
                            genre,
                            director,
                            writer,
                            actors,
                            plot
                        )
                        movieDao.insertMovies(insertMovie)  // Insert the movie

                        Toast.makeText(
                            this@SearchMovies,
                            "✔Movie saved to the database",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "❗Please search for a movie first", Toast.LENGTH_LONG).show()
                saveMovieClicked = false
            }
        }
    }

    override fun onSaveInstanceState(outState : Bundle) {   //Saving the current data as bundle
        super.onSaveInstanceState(outState)

        outState.putString("out", info)
        outState.putBoolean("btnStatus", searchMovieClicked)
    }

    /**
     * On restore instance state
     *
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {   //Getting the saved data(Restoring)
        super.onRestoreInstanceState(savedInstanceState)

        info = savedInstanceState.getString("out"," ")
        searchMovieClicked = savedInstanceState.getBoolean("btnStatus", false)
        textOut.text = info
    }

    @SuppressLint("SetTextI18n")
    suspend fun parseJSON(stb: StringBuilder): String {

        val json = JSONObject(stb.toString()) //convert string to JSON object
        if (json.has("Error")) {
            searchMovieClicked = false
            textOut.setTextColor(Color.RED)
            return " ❗No result found. Please try again after checking the movie name. "
        }

        // Getting the value of the relevant key.
        title = json["Title"].toString()
        year = json["Year"].toString()
        rated = json["Rated"].toString()
        released = json["Released"].toString()
        runtime = json["Runtime"].toString()
        genre = json["Genre"].toString()
        director = json["Director"].toString()
        writer = json["Writer"].toString()
        actors = json["Actors"].toString()
        plot = json["Plot"].toString()

        info = " Title: $title\n Year: $year\n Rated: $rated\n Released: $released\n Runtime: $runtime\n Genre: $genre\n Director: $director\n Writer: $writer\n Actors: $actors\n Plot: $plot"
        return info
    }
}