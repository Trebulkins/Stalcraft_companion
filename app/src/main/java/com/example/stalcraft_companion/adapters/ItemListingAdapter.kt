package com.example.stalcraft_companion.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.api.RetrofitClient
import com.example.stalcraft_companion.api.schemas.ListingResponse
import com.squareup.picasso.Picasso

class ItemListingAdapter(private var itemList: List<ListingResponse>, private var context: Context) : RecyclerView.Adapter<ItemListingAdapter.ItemDBHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDBHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        val viewHolder = ItemDBHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemDBHolder, pos: Int) {
        holder.itemTitle.text = itemList[pos].name.toString()
        holder.itemTitle.setTextColor(Color.parseColor(itemList[pos].color))
        holder.itemCategory.text = itemList[pos].data.substringBeforeLast('/')
        holder.itemRarity.setBackgroundColor(Color.parseColor(itemList[pos].color))
        holder.itemState.text = "@string/${itemList[pos].status!!.state}"
        when (itemList[pos].status!!.state) {
            "PERSONAL_ON_USE" -> holder.itemStateImg.setImageResource(R.drawable.personal_on_use)
            "NON_DROP" -> holder.itemStateImg.setImageResource(R.drawable.non_drop)
            "PERSONAL_DROP_ON_GET" -> holder.itemStateImg.setImageResource(R.drawable.personal_drop_on_get)
        }

        Picasso.get().load(RetrofitClient.DATABASE_URL.substringBeforeLast('/') + itemList[pos].icon).into(holder.itemIcon)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getItemAtPosition(pos: Int): ListingResponse {
        return itemList[pos]
    }

    inner class ItemDBHolder(v: View) : RecyclerView.ViewHolder(v) {
        var itemTitle: TextView = v.findViewById(R.id.item_title)
        var itemCategory: TextView = v.findViewById(R.id.item_category)
        var itemIcon: ImageView = v.findViewById(R.id.item_icon)
        var itemRarity: View = v.findViewById(R.id.item_rarity_color)
        var itemState: TextView = v.findViewById(R.id.item_state)
        var itemStateImg: ImageView = v.findViewById(R.id.item_state_img)
    }
}