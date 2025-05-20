package com.example.stalcraft_companion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.MainActivity
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.api.schemas.Item
import com.squareup.picasso.Picasso

class ItemDBAdapter(var itemsList: List<Item>, var context: Context, var listener: MainActivity.RecyclerItemListener) : RecyclerView.Adapter<ItemDBAdapter.SearchItemsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)

        val viewHolder = ItemDBHolder(view)
        view.setOnClickListener { v -> listener.onItemClick(v, viewHolder.adapterPosition) }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemDBHolder, position: Int) {
        holder.titleTextView.text = itemsList[position].nameRu
        holder.originalTitleTextView.text = itemsList[position].nameOriginal
        holder.releaseDateTextView.text = itemsList[position].year.toString()

        if (itemList[position].posterUrl != null) {
            Picasso.get().load(RetrofitClient.GITHUB_URL + itemList[position].posterUrl).into(holder.itemImageView)
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun getItemAtPosition(pos: Int): Item {
        return itemsList[pos]
    }

    inner class ItemDBHolder(v: View) : RecyclerView.ViewHolder(v) {
        var itemTitle: TextView = v.findViewById(R.id.item_title)
        var itemCategory: TextView = v.findViewById(R.id.item_category)
        var itemIcon: ImageView = v.findViewById(R.id.item_icon)
        var itemRarity: View = v.findViewById(R.id.item_rarity_color)
        var itemState: TextView = v.findViewById(R.id.item_state)
        var itemStateImg: ImageView = v.findViewById(R.id.item_state_img)
        var itemWeight: TextView = v.findViewById(R.id.item_weight)

        init {
            v.setOnClickListener { v: View ->
                listener.onItemClick(v, adapterPosition)
            }
        }
    }
}