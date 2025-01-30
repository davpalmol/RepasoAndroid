package com.example.apprepaso

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apprepaso.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() , onClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        val notasEntityLists: List<Note> = listOf(
            Note(
                title = "Deberes de matemáticas",
                content = "Resolver los ejercicios de la página 45.",
                date = "2023, 9, 25" // Fecha: 25 de octubre de 2023
            ),
            Note(
                title = "Lista de compras",
                content = "Comprar leche, pan, huevos y frutas.",
                date = "2023, 9, 26" // Fecha: 26 de octubre de 2023
            ),
            Note(
                title = "Reunión de trabajo",
                content = "Preparar la presentación para la reunión con el cliente.",
                date = "2023, 9, 27" // Fecha: 26 de octubre de 2023
            )
        )

        Thread {
            val noteDAO = NoteApplication.database.noteDAO()

            val notasRecuperadas = noteDAO.getAllNota()

            if (notasRecuperadas.isEmpty()) {
                notasEntityLists.forEach { note ->
                    noteDAO.addNota(note)
                }
            }

            runOnUiThread {
                if (notasRecuperadas.isNotEmpty()){
                    mAdapter.setNotas(notasRecuperadas.toMutableList())
                }
            }

        }.start()

        binding.boton.setOnClickListener {
            val intent = Intent(this, DetailsActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        mAdapter = NoteAdapter(mutableListOf(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = mAdapter
    }

    override fun onClick(nota: Note) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("Nota", nota)
        startActivity(intent)
    }


}