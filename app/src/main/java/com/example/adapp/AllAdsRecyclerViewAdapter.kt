package com.example.adapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.example.adapp.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class AllAdsRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<AllAdsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_all_ads, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.adTitle.setText(item.content)
        holder.adPrice.setText(item.id)
        holder.adDescription.setText(item.id)
        holder.adPrice.setText(item.id)
        holder.adLocation.setText(item.id)
        holder.adContactTV.setText(item.id)


    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val adTitle: TextView = view.findViewById(R.id.adTitleTV)
        val adPrice: TextView = view.findViewById(R.id.adPriceTV)
        val adLocation: TextView = view.findViewById(R.id.adLocationTV)
        val adDescription: TextView = view.findViewById(R.id.adDescriptionTV)
        val adContactTV: TextView = view.findViewById(R.id.adContactTV)
        val adImage:ImageView=view.findViewById(R.id.adImageIV)

    }
}