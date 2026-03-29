package com.example.desafio02dsm

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditDestinationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_destination)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        
        val tvImageTitle = findViewById<TextView>(R.id.tvImageTitle)
        val etName = findViewById<EditText>(R.id.etName)
        val etCountry = findViewById<EditText>(R.id.etCountry)
        val etPrice = findViewById<EditText>(R.id.etPrice)

        val destName = intent.getStringExtra("DESTINATION_NAME") ?: ""
        val destCountry = intent.getStringExtra("DESTINATION_COUNTRY") ?: ""
        val destPrice = intent.getStringExtra("DESTINATION_PRICE") ?: ""

        tvImageTitle.text = destName
        etName.setText(destName)
        etCountry.setText(destCountry)
        etPrice.setText(destPrice.replace("$", "").replace(",", ""))

        btnBack.setOnClickListener {
            finish()
        }

        val btnSave = findViewById<AppCompatButton>(R.id.btnSave)
        btnSave.setOnClickListener {
            // Regresar al menu principal luego de "guardar"
            finish()
        }

        val btnDiscard = findViewById<AppCompatButton>(R.id.btnDiscard)
        btnDiscard.setOnClickListener {
            // Regresar o descartar cambios
            finish()
        }

        val btnDelete = findViewById<AppCompatButton>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            // Eliminar y regresar
            finish()
        }
    }
}