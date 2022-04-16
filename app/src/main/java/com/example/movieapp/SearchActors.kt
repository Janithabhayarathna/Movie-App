package com.example.movieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SearchActors : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_actors)

        val actor = findViewById<EditText>(R.id.actorName)
        val searchBtn = findViewById<Button>(R.id.btnSearch)

        searchBtn.setOnClickListener {
            val actorName = actor!!.text.toString().trim().lowercase()
            
        }
    }
}