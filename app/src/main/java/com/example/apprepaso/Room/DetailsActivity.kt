package com.example.apprepaso.Room

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.apprepaso.R
import com.example.apprepaso.Settings.SettingsActivity
import com.example.apprepaso.databinding.ActivityDetailsBinding
import java.util.Date

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
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

        val nota = intent.getSerializableExtra("Nota") as? Note

        if (nota != null) {
            binding.editTitle.setText(nota.title)
            binding.editDescription.setText(nota.content)
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnUpdate.visibility = View.VISIBLE
            binding.btnInsert.visibility = View.INVISIBLE
        } else {
            binding.editTitle.setText("")
            binding.editDescription.setText("")
            binding.btnDelete.visibility = View.INVISIBLE
            binding.btnUpdate.visibility = View.INVISIBLE
            binding.btnInsert.visibility = View.VISIBLE
        }

        binding.btnInsert.setOnClickListener {
            val nota = Note(
                title = binding.editTitle.text.toString(),
                content = binding.editDescription.text.toString(),
                date = Date().toString()
            )

            Thread {
                NoteApplication.database.noteDAO().addNota(nota)
            }.start()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnUpdate.setOnClickListener {
                if (nota != null) {
                    val notaX = Note(
                        id = nota.id,
                        title = binding.editTitle.text.toString(),
                        content = binding.editDescription.text.toString(),
                        date = Date().toString()
                    )
                    Thread {
                        NoteApplication.database.noteDAO().updateNota(notaX)
                    }.start()
                }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnDelete.setOnClickListener {
            Thread {
                if (nota != null) {
                    NoteApplication.database.noteDAO().deleteNota(nota)
                }
            }.start()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}