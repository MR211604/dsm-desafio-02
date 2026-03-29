package com.example.desafio02dsm

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.Window
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Typeface

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
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_delete_destination)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

            val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
            val btnConfirm = dialog.findViewById<AppCompatButton>(R.id.btnConfirmDelete)
            val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancelDelete)

            val fullText = "Esto eliminará permanentemente $destName y todas las reservas asociadas de tu itinerario."
            val spannable = SpannableStringBuilder(fullText)
            val start = fullText.indexOf(destName)
            if (start != -1) {
                // Apply bold style directly, using the font family from XML for the text.
                spannable.setSpan(StyleSpan(Typeface.BOLD), start, start + destName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvMessage.text = spannable

            btnConfirm.setOnClickListener {
                dialog.dismiss()
                // Logica de borrar vacia
                finish()
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}