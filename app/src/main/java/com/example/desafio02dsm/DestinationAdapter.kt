package com.example.desafio02dsm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DestinationAdapter(private val destinations: List<Destination>) :
    RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    class DestinationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvCountry: TextView = view.findViewById(R.id.tvCountry)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivDestination: ImageView = view.findViewById(R.id.ivDestination)
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
        holder.tvPrice.text = dest.price
        holder.tvRating.text = dest.rating
        holder.tvDescription.text = dest.description
        // Fallback or solid color while there is no URL image loader
        holder.ivDestination.setBackgroundResource(dest.colorRes)
    }

    override fun getItemCount(): Int = destinations.size
}