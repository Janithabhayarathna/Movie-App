package com.example.movieapp

import androidx.room.Database
import androidx.room.RoomDatabase

abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}