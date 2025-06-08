package com.example.stalcraft_companion.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.ApiClient
import com.example.stalcraft_companion.data.modles.CategoryGroup
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.SubcategoryGroup
import com.example.stalcraft_companion.data.modles.TranslationString
import com.squareup.picasso.Picasso

object CategoryUtils {
    fun getMainCategory(fullCategory: String): String {
        return fullCategory.split('/').first()
    }

    fun getSubcategory(fullCategory: String): String? {
        val parts = fullCategory.split('/')
        return if (parts.size > 1) parts[1] else null
    }

    fun groupItemsByCategories(items: List<Item>): List<CategoryGroup> {
        return items.groupBy { getMainCategory(it.category) }
            .map { (mainCategory, items) ->
                val subcategories = items
                    .filter { getSubcategory(it.category) != null }
                    .groupBy { getSubcategory(it.category)!! }
                    .map { (subcategory, items) -> SubcategoryGroup(subcategory, items) }

                val itemsWithoutSubcategory = items.filter { getSubcategory(it.category) == null }

                CategoryGroup(
                    mainCategory = mainCategory,
                    subcategories = subcategories,
                    directItems = itemsWithoutSubcategory
                )
            }
            .sortedBy { it.mainCategory }
    }
}

class CategoryAdapter(
    private val context: Context,
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_SUBCATEGORY = 1
        private const val TYPE_ITEM = 2
    }

    private var items: List<Any> = emptyList()
    private var expandedCategory: String? = null

    fun submitCategories(categories: List<CategoryGroup>) {
        val newItems = mutableListOf<Any>()

        categories.forEach { category ->
            newItems.add(category)

            if (category.mainCategory == expandedCategory) {
                if (category.directItems.isNotEmpty()) {
                    newItems.addAll(category.directItems)
                }

                category.subcategories.forEach { subcategory ->
                    newItems.add(subcategory)
                    newItems.addAll(subcategory.items)
                }
            }
        }

        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CategoryGroup -> TYPE_CATEGORY
            is SubcategoryGroup -> TYPE_SUBCATEGORY
            is Item -> TYPE_ITEM
            else -> throw IllegalArgumentException("Unknown type")
        }
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
            else -> ItemViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_layout, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> bindCategory(holder, position)
            is SubcategoryViewHolder -> bindSubcategory(holder, position)
            is ItemViewHolder -> bindItem(holder, position)
        }
    }

    private fun bindCategory(holder: CategoryViewHolder, position: Int) {
        val category = items[position] as CategoryGroup
        holder.bind(category.mainCategory)

        holder.itemView.setOnClickListener {
            expandedCategory = if (expandedCategory == category.mainCategory) {
                null
            } else {
                category.mainCategory
            }
            submitCategories(items.filterIsInstance<CategoryGroup>())
        }
    }

    private fun bindSubcategory(holder: SubcategoryViewHolder, position: Int) {
        val subcategory = items[position] as SubcategoryGroup
        holder.bind(subcategory.subcategoryName)
    }

    private fun bindItem(holder: ItemViewHolder, position: Int) {
        val item = items[position] as Item
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount(): Int = items.size

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(title: String) {
            itemView.findViewById<TextView>(R.id.tvTitle).text = title
        }
    }

    inner class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(title: String) {
            itemView.findViewById<TextView>(R.id.tvTitle).text = title
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Item) {
            itemView.findViewById<TextView>(R.id.item_title).text = when (val name = item.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            when (item.color) {
                "DEFAULT" -> {
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(android.graphics.Color.parseColor("#eeeeee"))
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(android.graphics.Color.parseColor("#eeeeee"))
                }
                "RANK_NEWBIE" -> {
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(android.graphics.Color.parseColor("#7def7d"))
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(android.graphics.Color.parseColor("#7def7d"))
                }
                "RANK_STALKER" -> {
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(android.graphics.Color.parseColor("#8d8dff"))
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(android.graphics.Color.parseColor("#8d8dff"))
                }
                "RANK_VETERAN" -> {
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(android.graphics.Color.parseColor("#d968c4"))
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(android.graphics.Color.parseColor("#d968c4"))
                }
                "RANK_MASTER" -> {
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(android.graphics.Color.parseColor("#ff5767"))
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(android.graphics.Color.parseColor("#ff5767"))
                }
                "RANK_LEGEND" -> {
                    itemView.findViewById<TextView>(R.id.item_title).setTextColor(android.graphics.Color.parseColor("#ffdd66"))
                    itemView.findViewById<View>(R.id.item_rarity_color).setBackgroundColor(android.graphics.Color.parseColor("#ffdd66"))
                }
            }
            itemView.findViewById<TextView>(R.id.item_category).text = item.category
            itemView.findViewById<TextView>(R.id.item_state).setText(when (item.status.state) {
                "NONE" -> R.string.STATE_NONE
                "NON_DROP" -> R.string.STATE_NON_DROP
                "PERSONAL_ON_USE" -> R.string.STATE_PERSONAL_ON_USE
                "PERSONAL_DROP_ON_GET" -> R.string.STATE_PERSONAL_DROP_ON_GET
                else -> R.string.STATE_NONE
            })
            itemView.findViewById<ImageView>(R.id.item_state_img).setImageResource(when (item.status.state) {
                "NONE" -> R.drawable.invisible
                "NON_DROP" -> R.drawable.non_drop
                "PERSONAL_ON_USE" -> R.drawable.personal_on_use
                "PERSONAL_DROP_ON_GET" -> R.drawable.personal_drop_on_get
                else -> R.drawable.invisible
            })
            Picasso.get().load(ApiClient.DATABASE_BASE_URL + item.iconPath).into(itemView.findViewById<ImageView>(R.id.item_icon))

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}