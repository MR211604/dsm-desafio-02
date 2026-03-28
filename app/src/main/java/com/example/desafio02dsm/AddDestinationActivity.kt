package com.example.desafio02dsm

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddDestinationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_destination)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup back button
        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Close this activity and return to the previous one
            finish()
        }
        
        // Mock save action
        val btnSave = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btnSaveDestination)
        btnSave?.setOnClickListener {
            Toast.makeText(this, "Destination saved successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}