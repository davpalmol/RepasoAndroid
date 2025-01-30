package com.example.apprepaso

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notas")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val date: String
): Serializable {
    constructor(title: String, content: String, date: String):
            this(0, title, content, date)
}


