package com.example.desafio02dsm

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var rvDestinations: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var btnMore: ImageView
    private val destinationList = mutableListOf<Destination>()
    private lateinit var adapter: DestinationAdapter

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
        btnMore = findViewById(R.id.btnMore)

        adapter = DestinationAdapter(destinationList)
        rvDestinations.layoutManager = LinearLayoutManager(this)
        rvDestinations.adapter = adapter

        fetchDestinationsFromFirebase()

        fabAdd.setOnClickListener {
            val intent = Intent(this, AddDestinationActivity::class.java)
            startActivity(intent)
        }

        btnMore.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.menu_main, popup.menu)
            popup.setOnMenuItemClickListener { item ->
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
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    private fun fetchDestinationsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("destinations")
            .addSnapshotListener { result, exception ->
                if (exception != null) {
                    Toast.makeText(this, "Error load destinations: ${exception.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (result != null) {
                    destinationList.clear()
                    for (document in result) {
                        try {
                            val destination = document.toObject(Destination::class.java)
                            destination.id = document.id
                            destinationList.add(destination)
                        } catch (e: Exception) {
                            Log.e("MainActivity", "Error parsing destination", e)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }
}