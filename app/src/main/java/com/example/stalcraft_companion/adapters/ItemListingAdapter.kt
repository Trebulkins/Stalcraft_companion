package com.example.stalcraft_companion.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.api.ApiClient
import com.example.stalcraft_companion.api.schemas.CategoryGroup
import com.example.stalcraft_companion.api.schemas.ListingItem
import com.example.stalcraft_companion.api.schemas.SubcategoryGroup
import com.squareup.picasso.Picasso

@SuppressLint("SetTextI18n")
class ItemListingAdapter(
    private val groups: List<CategoryGroup>,
    private val onItemClick: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_SUBCATEGORY = 1
        private const val TYPE_ITEM = 2
    }

    override fun getItemViewType(position: Int): Int {
        var pos = 0
        groups.forEach { group ->
            if (position == pos) return TYPE_CATEGORY
            pos++

            if (group.isExpanded) {
                if (group.hasSubcategories()) {
                    group.subcategories.forEach { subgroup ->
                        if (position == pos) return TYPE_SUBCATEGORY
                        pos++

                        if (subgroup.isExpanded) {
                            if (position < pos + subgroup.items.size) return TYPE_ITEM
                            pos += subgroup.items.size
                        }
                    }
                } else {
                    if (position < pos + group.getAllItems().size) return TYPE_ITEM
                    pos += group.getAllItems().size
                }
            }
        }
        throw IllegalArgumentException("Invalid position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> CategoryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_category, parent, false)
            )
            TYPE_SUBCATEGORY -> SubcategoryViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_subcategory, parent, false)
            )
            else -> ListingItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_layout, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var pos = 0
        groups.forEach { group ->
            if (position == pos) {
                (holder as CategoryViewHolder).bind(group)
                holder.itemView.setOnClickListener {
                    group.isExpanded = !group.isExpanded
                    notifyDataSetChanged()
                }
                return
            }
            pos++

            if (group.isExpanded) {
                if (group.hasSubcategories()) {
                    group.subcategories.forEach { subgroup ->
                        if (position == pos) {
                            (holder as SubcategoryViewHolder).bind(subgroup)
                            holder.itemView.setOnClickListener {
                                subgroup.isExpanded = !subgroup.isExpanded
                                notifyDataSetChanged()
                            }
                            return
                        }
                        pos++

                        if (subgroup.isExpanded) {
                            if (position < pos + subgroup.items.size) {
                                val item = subgroup.items[position - pos]
                                (holder as ListingItemViewHolder).bind(item)
                                holder.itemView.setOnClickListener { onItemClick(item) }
                                return
                            }
                            pos += subgroup.items.size
                        }
                    }
                } else {
                    if (position < pos + group.getAllItems().size) {
                        val item = group.getAllItems()[position - pos]
                        (holder as ListingItemViewHolder).bind(item)
                        holder.itemView.setOnClickListener { onItemClick(item) }
                        return
                    }
                    pos += group.getAllItems().size
                }
            }
        }
    }

    override fun getItemCount(): Int {
        var count = groups.size
        groups.forEach { group ->
            if (group.isExpanded) {
                if (group.hasSubcategories()) {
                    count += group.subcategories.size
                    group.subcategories.forEach { subgroup ->
                        if (subgroup.isExpanded) {
                            count += subgroup.items.size
                        }
                    }
                } else {
                    count += group.getAllItems().size
                }
            }
        }
        return count
    }

    // ViewHolders
    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tvTitle)
        private val icon: ImageView = view.findViewById(R.id.ivIcon)

        fun bind(group: CategoryGroup) {
            icon.rotation = if (group.isExpanded) 180f else 0f
        }
    }

    inner class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tvTitle)
        private val icon: ImageView = view.findViewById(R.id.ivIcon)

        fun bind(subgroup: SubcategoryGroup) {
            icon.rotation = if (subgroup.isExpanded) 180f else 0f
            itemView.setPadding(16, 0, 0, 0)
        }
    }

    inner class ListingItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(item: ListingItem) {
            itemView.findViewById<TextView>(R.id.item_title).text = item.name.lines?.ru
            itemView.findViewById<TextView>(R.id.item_category).text = item.category
            itemView.setPadding(if (item.hasSubcategory) 32 else 16,0,0,0)

            when (item.color) {
                "DEFAULT" -> {
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#808080"))
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#808080"))
                }
                "RANK_NEWBIE" -> {
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#9ACD32"))
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#9ACD32"))
                }
                "RANK_STALKER" -> {
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#1E90FF"))
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#1E90FF"))
                }
                "RANK_VETERAN" -> {
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#BA55D3"))
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#BA55D3"))
                }
                "RANK_MASTER" -> {
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#B22222"))
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#B22222"))
                }
                "RANK_LEGEND" -> {
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(Color.parseColor("#FFD700"))
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(Color.parseColor("#FFD700"))
                }
            }

            when (item.status.state) {
                "PERSONAL_ON_USE" -> {
                    itemView.findViewById<ImageView>(R.id.item_state_img).setImageResource(R.drawable.personal_on_use)
                    itemView.findViewById<TextView>(R.id.item_state).setText(R.string.PERSONAL_ON_USE)
                }
                "NON_DROP" -> {
                    itemView.findViewById<ImageView>(R.id.item_state_img).setImageResource(R.drawable.non_drop)
                    itemView.findViewById<TextView>(R.id.item_state).setText(R.string.NON_DROP)
                }
                "PERSONAL_DROP_ON_GET" -> {
                    itemView.findViewById<ImageView>(R.id.item_state_img).setImageResource(R.drawable.personal_drop_on_get)
                    itemView.findViewById<TextView>(R.id.item_state).setText(R.string.PERSONAL_DROP_ON_GET)
                }
            }

            Picasso.get().load(ApiClient.BASE_URL.substringBeforeLast('/') + item.icon).into(itemView.findViewById<ImageView>(R.id.item_icon))
        }
    }
}