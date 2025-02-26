package com.example.apprepaso.Room

import android.app.Application
import androidx.room.Room

class NoteApplication : Application(){
    companion object {
        lateinit var database: NoteDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, NoteDatabase::class.java,
            "NoteDatabase")
            .build()
    }
}