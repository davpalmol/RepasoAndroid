package com.example.apprepaso

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {

    @Query("SELECT * FROM notas")
    fun getAllNota() : MutableList<Note>

    @Query("SELECT * FROM notas WHERE id = notas.id ")
    fun getNota() : Note

    @Insert
    fun addNota(nota: Note)

    @Update
    fun updateNota(nota: Note)

    @Delete
    fun deleteNota(nota: Note)

}