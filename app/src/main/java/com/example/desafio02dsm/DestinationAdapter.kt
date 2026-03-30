package com.example.desafio02dsm

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DestinationAdapter(private val destinations: List<Destination>) :
    RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    class DestinationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCountry: TextView = view.findViewById(R.id.tvCountry)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivDestination: ImageView = view.findViewById(R.id.ivDestination)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destination, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val dest = destinations[position]
        holder.tvName.text = dest.name
        holder.tvCountry.text = dest.country
        holder.tvPrice.text = "$${dest.price}"
        holder.tvDescription.text = dest.highlights

        if (dest.imageBase64.isNotEmpty()) {
            try {
                // If there's a prefix like "data:image/jpeg;base64,", remove it
                val base64Image = if (dest.imageBase64.contains(",")) {
                    dest.imageBase64.substringAfter(",")
                } else {
                    dest.imageBase64
                }
                val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                holder.ivDestination.setImageBitmap(decodedByte)
                holder.ivDestination.setBackgroundResource(android.R.color.transparent)
            } catch (e: Exception) {
                holder.ivDestination.setImageResource(R.color.primary_container)
            }
        } else {
            holder.ivDestination.setImageResource(R.color.primary_container)
        }
        
        holder.btnEdit.setOnClickListener {
            val context = it.context
            val intent = Intent(context, EditDestinationActivity::class.java)
            intent.putExtra("DESTINATION_ID", dest.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = destinations.size
}