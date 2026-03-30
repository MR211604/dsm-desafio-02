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
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream

class EditDestinationActivity : AppCompatActivity() {

    private var destinationId: String = ""
    private var currentImageBase64: String = ""
    private var base64Image: String = ""
    
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            val ivImage = findViewById<ImageView>(R.id.ivDestination)
            ivImage.setImageBitmap(bitmap)
            ivImage.setBackgroundResource(android.R.color.transparent)

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_destination)
        
        destinationId = intent.getStringExtra("DESTINATION_ID") ?: ""

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
        val etDuration = findViewById<EditText>(R.id.etDuration)
        val etHighlights = findViewById<EditText>(R.id.etHighlights)
        val ivDestination = findViewById<ImageView>(R.id.ivDestination)
        val btnEditImage = findViewById<ImageView>(R.id.btnEditImage)

        if (destinationId.isNotEmpty()) {
            FirebaseFirestore.getInstance().collection("destinations").document(destinationId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val originName = document.getString("name") ?: ""
                        tvImageTitle.text = originName
                        etName.setText(originName)
                        etCountry.setText(document.getString("country") ?: "")
                        etPrice.setText((document.getString("price") ?: "").replace("$", "").replace(",", ""))
                        etDuration.setText(document.getString("duration") ?: "")
                        etHighlights.setText(document.getString("highlights") ?: "")

                        currentImageBase64 = document.getString("imageBase64") ?: ""
                        if (currentImageBase64.isNotEmpty()) {
                            try {
                                val base64Img = if (currentImageBase64.contains(",")) currentImageBase64.substringAfter(",") else currentImageBase64
                                val decodedString = Base64.decode(base64Img, Base64.DEFAULT)
                                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                                ivDestination.setImageBitmap(decodedByte)
                                ivDestination.setBackgroundResource(android.R.color.transparent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
        }

        btnEditImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnBack.setOnClickListener {
            finish()
        }

        val btnSave = findViewById<AppCompatButton>(R.id.btnSave)
        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val country = etCountry.text.toString()
            val price = etPrice.text.toString()
            val duration = etDuration.text.toString()
            val highlights = etHighlights.text.toString()
            
            val imageToSave = if (base64Image.isNotEmpty()) base64Image else currentImageBase64

            val destinationMap = hashMapOf(
                "name" to name,
                "country" to country,
                "price" to price,
                "duration" to duration,
                "highlights" to highlights,
                "imageBase64" to imageToSave
            )

            FirebaseFirestore.getInstance().collection("destinations").document(destinationId)
                .update(destinationMap as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this, "Destino actualizado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
        }

        val btnDiscard = findViewById<AppCompatButton>(R.id.btnDiscard)
        btnDiscard.setOnClickListener {
            // Regresar o descartar cambios
            finish()
        }

        val btnDelete = findViewById<AppCompatButton>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            val currentName = tvImageTitle.text.toString()
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_delete_destination)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.setLayout((resources.displayMetrics.widthPixels * 0.90).toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)

            val tvMessage = dialog.findViewById<TextView>(R.id.tvDialogMessage)
            val btnConfirm = dialog.findViewById<AppCompatButton>(R.id.btnConfirmDelete)
            val btnCancel = dialog.findViewById<AppCompatButton>(R.id.btnCancelDelete)

            val fullText = "Esto eliminará permanentemente $currentName y todas las reservas asociadas de tu itinerario."
            val spannable = SpannableStringBuilder(fullText)
            val start = fullText.indexOf(currentName)
            if (start != -1 && currentName.isNotEmpty()) {
                // Apply bold style directly, using the font family from XML for the text.
                spannable.setSpan(StyleSpan(Typeface.BOLD), start, start + currentName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tvMessage.text = spannable

            btnConfirm.setOnClickListener {
                dialog.dismiss()
                FirebaseFirestore.getInstance().collection("destinations").document(destinationId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Destino eliminado", Toast.LENGTH_SHORT).show()
                        val mainIntent = Intent(this, MainActivity::class.java)
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(mainIntent)
                        finish()
                    }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}