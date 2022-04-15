package com.example.movieapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovies : AppCompatActivity() {
    lateinit var text:TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        val movie = findViewById<EditText>(R.id.movieName)
        text = findViewById<TextView>(R.id.output)
        val btn = findViewById<Button>(R.id.retreiveBtn)
        val btnSave = findViewById<Button>(R.id.saveMovieBtn)

        btn.setOnClickListener {

            var stb = StringBuilder()
            val movieName = movie.text.toString()
            val urlString = "https://www.omdbapi.com/?t=$movieName&apikey=ff95b66";
            val url = URL(urlString)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection

            runBlocking {
                launch {
                    withContext(Dispatchers.IO) {
                        val bf = BufferedReader(InputStreamReader(con.inputStream))
                        var line: String? = bf.readLine()
                        while (line != null) {
                            stb.append(line + "\n")
                            line = bf.readLine()
                        }
                        parseJSON(stb)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    suspend fun parseJSON(stb: java.lang.StringBuilder) {

        text.text = "Loading..."
        val jsonObject = JSONObject(stb.toString())
        val title = jsonObject.getString("Title").toString()
        val year = jsonObject.getString("Year").toString()
        val rated = jsonObject.getString("Rated").toString()
        val released = jsonObject.getString("Released").toString()
        val runtime = jsonObject.getString("Runtime").toString()
        val genre = jsonObject.getString("Genre").toString()
        val director = jsonObject.getString("Director").toString()
        val writer = jsonObject.getString("Writer").toString()
        val actors = jsonObject.getString("Actors").toString()
        val plot = jsonObject.getString("Plot").toString()
        text.text = "Title: $title\nYear: $year\nRated: $rated\nReleased: $released\nRuntime: $runtime\nGenre: $genre\nDirector: $director\nWriter: $writer\nActors: $actors\nPlot: $plot"
    }
}