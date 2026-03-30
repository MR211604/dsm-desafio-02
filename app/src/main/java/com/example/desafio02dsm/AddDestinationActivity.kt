package com.example.desafio02dsm

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.firestore.FirebaseFirestore

class AddDestinationActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null

    private lateinit var ivCover: ImageView
    private lateinit var llUploadPrompt: LinearLayout
    private lateinit var etDestinationName: EditText
    private lateinit var etDestinationCountry: AutoCompleteTextView
    private lateinit var etDestinationPrice: EditText
    private lateinit var etDestinationDuration: EditText
    private lateinit var etDestinationHighlights: EditText
    private lateinit var btnSave: AppCompatButton

    private val firestore = FirebaseFirestore.getInstance()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedImageUri = uri
            ivCover.setImageURI(uri)
            llUploadPrompt.visibility = View.GONE
        }
    }

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
        
        ivCover = findViewById(R.id.ivCover)
        llUploadPrompt = findViewById(R.id.llUploadPrompt)
        etDestinationName = findViewById(R.id.etDestinationName)
        etDestinationCountry = findViewById(R.id.etDestinationCountry)
        etDestinationPrice = findViewById(R.id.etDestinationPrice)
        etDestinationDuration = findViewById(R.id.etDestinationDuration)
        etDestinationHighlights = findViewById(R.id.etDestinationHighlights)
        btnSave = findViewById(R.id.btnSaveDestination)

        findViewById<View>(R.id.imageContainer).setOnClickListener {
            pickImage.launch("image/*")
        }
        
        setupCountryDropdown()
        
        btnSave.setOnClickListener {
            saveDestination()
        }
    }

    private fun setupCountryDropdown() {
        val countries = arrayOf(
            "Francia", "Italia", "España", "Japón", "Estados Unidos",
            "México", "Reino Unido", "Tailandia", "Turquía", "Grecia",
            "Alemania", "Brasil", "Perú", "Emiratos Árabes Unidos", "Indonesia",
            "Egipto", "Maldivas", "Suiza"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countries)
        etDestinationCountry.setAdapter(adapter)

        findViewById<ImageView>(R.id.ivDropdown).setOnClickListener {
            etDestinationCountry.showDropDown()
        }
        etDestinationCountry.setOnClickListener {
            etDestinationCountry.showDropDown()
            // Clear current text if the user clicks so they can see all options easily
            // or just show the dropdown with current filtering.
        }
    }

    private fun saveDestination() {
        val name = etDestinationName.text.toString().trim()
        val country = etDestinationCountry.text.toString().trim()
        val price = etDestinationPrice.text.toString().trim()
        val duration = etDestinationDuration.text.toString().trim()
        val highlights = etDestinationHighlights.text.toString().trim()

        if (name.isEmpty() || country.isEmpty() || price.isEmpty() || duration.isEmpty()) {
            Toast.makeText(this, "Por favor llena los campos requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        btnSave.isEnabled = false
        btnSave.text = "Guardando..."

        if (selectedImageUri != null) {
            val base64Image = getBase64Image(selectedImageUri!!)
            if (base64Image != null) {
                saveDataToFirestore(name, country, price, duration, highlights, base64Image)
            } else {
                Toast.makeText(this, "Error procesando la imagen. Asegúrate de que no sea muy pesada.", Toast.LENGTH_SHORT).show()
                btnSave.isEnabled = true
                btnSave.text = "Guardar cambios"
            }
        } else {
            saveDataToFirestore(name, country, price, duration, highlights, null)
        }
    }

    private fun getBase64Image(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)
            
            // Escalar la imagen si es muy grande para no exceder el límite de 1MB por documento en Firestore
            val maxDimension = 800
            val width = originalBitmap.width
            val height = originalBitmap.height
            
            val scaledBitmap = if (width > maxDimension || height > maxDimension) {
                val ratio = width.toFloat() / height.toFloat()
                val newWidth = if (ratio > 1) maxDimension else (maxDimension * ratio).toInt()
                val newHeight = if (ratio > 1) (maxDimension / ratio).toInt() else maxDimension
                Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
            } else {
                originalBitmap
            }

            val outputStream = ByteArrayOutputStream()
            // Comprimir como JPEG al 70% de calidad
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val byteArray = outputStream.toByteArray()
            
            // Retornar en base64 junto con el prefijo util si se requiere luego para Glide o similar
            "data:image/jpeg;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveDataToFirestore(
        name: String, 
        country: String, 
        price: String, 
        duration: String, 
        highlights: String, 
        imageBase64: String?
    ) {
        val destination = hashMapOf(
            "name" to name,
            "country" to country,
            "price" to price,
            "duration" to duration,
            "highlights" to highlights,
            "imageBase64" to imageBase64
        )

        firestore.collection("destinations")
            .add(destination)
            .addOnSuccessListener {
                Toast.makeText(this, "¡Destino guardado con éxito!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error guardando: ${e.message}", Toast.LENGTH_SHORT).show()
                btnSave.isEnabled = true
                btnSave.text = "Guardar cambios"
            }
    }
}