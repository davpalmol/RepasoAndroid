package com.example.apprepaso.Room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apprepaso.Api.RazaGato
import com.example.apprepaso.Api.RetrofitInstance
import com.example.apprepaso.R
import com.example.apprepaso.Settings.SettingsActivity
import com.example.apprepaso.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() , onClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: NoteAdapter
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        // Configurar DrawerLayout con Toolbar
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_lista -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_configurar -> {
                    val intent = Intent(this, DetailsActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setupRecyclerView()

        val notas: MutableList<Note> = mutableListOf()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Obtener datos de la API
                val catBreeds = RetrofitInstance.api.getListadoRazas()
                val notas: MutableList<Note> = mutableListOf()
                catBreeds.take(10).forEach { cat ->
                    notas.add(Note(cat.name, cat.temperament, cat.origin))
                    Log.d("MainActivity", "Raza: ${cat.name}")
                }

                // Insertar datos en la base de datos
                val noteDAO = NoteApplication.database.noteDAO()
                if (noteDAO.getAllNota().isEmpty()) {
                    notas.forEach { note ->
                        noteDAO.addNota(note)
                    }
                }

                // Recuperar datos de la base de datos
                val notasRecuperadas = noteDAO.getAllNota()
                Log.d("MainActivity", "Notas recuperadas: $notasRecuperadas")

                // Actualizar el RecyclerView en el hilo principal
                withContext(Dispatchers.Main) {
                    mAdapter.setNotas(notasRecuperadas.toMutableList())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

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