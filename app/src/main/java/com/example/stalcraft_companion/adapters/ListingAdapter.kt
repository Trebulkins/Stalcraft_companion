package com.example.stalcraft_companion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.MainActivity
import com.example.stalcraft_companion.api.schemas.ListingItem
import com.squareup.picasso.Picasso

class ListingAdapter(var itemsList: List<ListingItem>, var context: Context, var listener: MainActivity.RecyclerItemListener) : RecyclerView.Adapter<ListingAdapter.SearchItemsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_item_details, parent, false)

        val viewHolder = SearchItemsHolder(view)
        view.setOnClickListener { v -> listener.onItemClick(v, viewHolder.adapterPosition) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: SearchItemsHolder, position: Int) {
        holder.titleTextView.text = itemList[position].nameRu
        holder.originalTitleTextView.text = itemList[position].nameOriginal
        holder.releaseDateTextView.text = itemList[position].year.toString()

        if (itemList[position].posterUrl != null) {
            Picasso.get().load(RetrofitClient.TMDB_BASE_URL + itemList[position].posterUrl).into(holder.itemImageView)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getItemAtPosition(pos: Int): item {
        return itemList[pos]
    }

    inner class SearchItemsHolder(v: View) : RecyclerView.ViewHolder(v) {
        var titleTextView: TextView = v.findViewById(R.id.title_textview)
        var originalTitleTextView: TextView = v.findViewById(R.id.original_title_textview)
        var releaseDateTextView: TextView = v.findViewById(R.id.release_date_textview)
        var itemImageView: ImageView = v.findViewById(R.id.item_imageview)

        init {
            v.setOnClickListener { v: View ->
                listener.onItemClick(v, adapterPosition)
            }
        }
    }
}