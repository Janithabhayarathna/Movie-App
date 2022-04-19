package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.*

class MovieBrowser : AppCompatActivity() {

    lateinit var output:TextView
    lateinit var input: EditText
    var out = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_browser)

        // Initialize the elements
        input = findViewById(R.id.input)
        output = findViewById(R.id.outputText)
        val browseBtn = findViewById<Button>(R.id.button)

        browseBtn.setOnClickListener {
            if(input.text.toString().length > 2) {  // To handle over amount of data
                getMovie()
            }else{
                Toast.makeText(applicationContext,"Please enter at least 3 characters!",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSaveInstanceState(outState : Bundle) {   //Saving the current data as bundle
        super.onSaveInstanceState(outState)

        outState.putString("out", out)
    }

    /**
     * On restore instance state
     *
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {   //Getting the saved data(Restoring)
        super.onRestoreInstanceState(savedInstanceState)

        out = savedInstanceState.getString("out"," ")
        output.text = out
    }
    private fun getMovie() {

        val movieName = input!!.text.toString().trim()
        if (movieName.isEmpty()) {
            Toast.makeText(this, "❗Please enter a movie name", Toast.LENGTH_LONG).show()
        }

        val urlString ="https://www.omdbapi.com/?s=*$movieName*&apikey=56835c20"
        var data = " "

        // start the fetching of data in the background
        runBlocking {
            withContext(Dispatchers.IO) {

                val stb = StringBuilder("") // StringBuilder to store the data

                val url = URL(urlString)
                val con = url.openConnection() as HttpURLConnection // open the connection
                val bf: BufferedReader
                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream)) // read the data
                } catch (e: IOException) {
                    e.printStackTrace()
                    return@withContext
                }

                var line = bf.readLine()
                while (line != null) {
                    stb.append(line)
                    line = bf.readLine()
                }

                data = parseJSON(stb)   // parse the data
            }
            output.text = data  // display the data
        }
    }

    private fun parseJSON(stb: StringBuilder): String {

        val json = JSONObject(stb.toString())   // convert the string to JSON
        if (json.has("Error")) {
            return " ❗No result found. Please try again after checking the movie name. "
        }
        val jsonArray: JSONArray = json.getJSONArray("Search")  // get the array of movies

        val movies = java.lang.StringBuilder()    // StringBuilder to store the movie details

        movies.append("\t\t\t\t\t\t 🎬 Movie List \n\n")

        for (i in 0 until jsonArray.length()) {
            val movie=jsonArray[i] as JSONObject    // get the movie
            movies.append("\t 📽️ " + movie.getString("Title") + "\n")
        }
        out = movies.toString()
        return out
    }
}