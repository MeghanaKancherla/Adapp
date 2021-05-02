package com.example.adapp.all_ads

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.adapp.R

import com.example.adapp.model.Advertisement

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class AllAdsRecyclerViewAdapter(
    private val values: List<Advertisement>,
    val listener: (Advertisement) -> Unit
) : RecyclerView.Adapter<AllAdsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_all_ads, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.adTitle.setText(item.title)
        //holder.adDescription.setText(item.description)
        holder.adPrice.setText((item.price.toString()))
        holder.adLocation.setText(item.location)
        //holder.adContactTV.setText(item.contact)

        if(item.imageUrl != null) {
            Glide.with(holder.itemView.context).load(Uri.parse(item.imageUrl)).into(holder.adImage)
        }

        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adTitle: TextView = view.findViewById(R.id.TitleTV)
        val adPrice: TextView = view.findViewById(R.id.PriceTV)
        val adLocation: TextView = view.findViewById(R.id.LocationTV)
        //val adDescription: TextView = view.findViewById(R.id.adDescriptionTV)
        //val adContactTV: TextView = view.findViewById(R.id.adContactTV)
        val adImage:ImageView=view.findViewById(R.id.ImageIV)

    }
}