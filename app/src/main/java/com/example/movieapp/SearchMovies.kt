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
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchMovies : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        val movie = findViewById<EditText>(R.id.movieName)
        val textOut = findViewById<TextView>(R.id.output)
        val btn = findViewById<Button>(R.id.retreiveBtn)
        val btnSave = findViewById<Button>(R.id.saveMovieBtn)

        btn?.setOnClickListener {

            val movieName = movie!!.text.toString().trim()
            val urlString = "https://www.omdbapi.com/?t=$movieName&apikey=ff95b66";

            var data: String = ""
            runBlocking {
                    withContext(Dispatchers.IO) {
                        val stb = StringBuilder("")

                        val url = URL(urlString)
                        val con = url.openConnection() as HttpURLConnection
                        val bf: BufferedReader
                        try {
                            bf = BufferedReader(InputStreamReader(con.inputStream))
                        } catch (e: IOException) {
                            e.printStackTrace()
                            return@withContext
                        }
                        var line = bf.readLine()
                        while (line != null) {
                            stb.append(line)
                            line = bf.readLine()
                        }
                        data = parseJSON(stb)

                    }

            }
            textOut?.text = data
        }
    }

    @SuppressLint("SetTextI18n")
    suspend fun parseJSON(stb: StringBuilder): String {

        var info:String = "bla"
        val json = JSONObject(stb.toString()) //convert string to JSON object
        val title = json["Title"].toString()
        val year = json["Year"].toString()
        val rated = json["Rated"]
        val released = json["Released"]
        val runtime = json["Runtime"]
        val genre = json["Genre"]
        val director = json["Director"]
        val writer = json["Writer"]
        val actors = json["Actors"]
        val plot = json["Plot"]

        info = "Title: $title\n, Year: $year\n, Rated: $rated\n, Released: $released\n, Runtime: $runtime, Genre: $genre\n, Director: $director\n, Writer: $writer\n, Actors: $actors\n, Plot: $plot"
        return info
    }
}