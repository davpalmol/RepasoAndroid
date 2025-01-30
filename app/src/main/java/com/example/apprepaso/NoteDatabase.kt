package com.example.apprepaso

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun noteDAO():NoteDAO
}