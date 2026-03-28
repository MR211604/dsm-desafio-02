package com.example.desafio02dsm

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var rvDestinations: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private val destinationList = mutableListOf<Destination>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvDestinations = findViewById(R.id.rvDestinations)
        fabAdd = findViewById(R.id.fabAdd)

        setupRecyclerView()

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddDestinationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        destinationList.add(Destination("Santorini", "GREECE", "$1,850", "4.9", "Experience the iconic caldera views and whitewashed villages of the Cyclades. Perfect for romantic sunsets.", R.color.primary))
        destinationList.add(Destination("Ubud, Bali", "INDONESIA", "$1,200", "4.8", "Immerse yourself in the cultural heart of Bali. Surrounded by emerald rice paddies and sacred temples.", R.color.secondary))
        destinationList.add(Destination("Kyoto", "JAPAN", "$2,400", "5.0", "A timeless journey through imperial history. Discover zen gardens and traditional tea ceremonies.", R.color.primary_container))

        val adapter = DestinationAdapter(destinationList)
        rvDestinations.layoutManager = LinearLayoutManager(this)
        rvDestinations.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut().also {
                    Toast.makeText(
                        this,
                        "Sesion cerrada",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}