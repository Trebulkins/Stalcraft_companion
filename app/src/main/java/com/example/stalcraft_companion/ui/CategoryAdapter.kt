package com.example.stalcraft_companion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.ApiClient
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.TranslationString
import com.squareup.picasso.Picasso

class CategoryAdapter(
    private val onItemClick: (Item) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_SUBCATEGORY = 1
        private const val TYPE_ITEM = 2
    }

    private var items: List<Any> = emptyList()
    private var expandedCategories = mutableSetOf<String>()
    private var expandedSubcategories = mutableSetOf<String>()

    fun submitItems(allItems: List<Item>) {
        val categories = parseItemsToCategories(allItems)
        buildDisplayList(categories)
    }

    private fun parseItemsToCategories(items: List<Item>): Map<String, Category> {
        val categories = mutableMapOf<String, Category>()

        items.forEach { item ->
            val (mainCat, subCat) = parseCategory(item.category)

            if (!categories.containsKey(mainCat)) {
                categories[mainCat] = Category(mainCat, mutableMapOf())
            }

            if (subCat != null) {
                if (!categories[mainCat]!!.subcategories.containsKey(subCat)) {
                    categories[mainCat]!!.subcategories[subCat] = mutableListOf()
                }
                categories[mainCat]!!.subcategories[subCat]!!.add(item)
            } else {
                if (!categories[mainCat]!!.subcategories.containsKey("")) {
                    categories[mainCat]!!.subcategories[""] = mutableListOf()
                }
                categories[mainCat]!!.subcategories[""]!!.add(item)
            }
        }

        return categories
    }

    private fun parseCategory(fullCategory: String): Pair<String, String?> {
        return if (fullCategory.contains("/")) {
            val parts = fullCategory.split("/")
            parts[0] to parts[1]
        } else {
            fullCategory to null
        }
    }

    private fun buildDisplayList(categories: Map<String, Category>) {
        val newItems = mutableListOf<Any>()

        categories.keys.sorted().forEach { categoryName ->
            val category = categories[categoryName]!!
            newItems.add(category)

            if (categoryName in expandedCategories) {
                category.subcategories.keys.sorted().forEach { subcategoryName ->
                    if (subcategoryName.isNotEmpty()) {
                        newItems.add(Subcategory(categoryName, subcategoryName))

                        if ("$categoryName/$subcategoryName" in expandedSubcategories) {
                            newItems.addAll(category.subcategories[subcategoryName]!!)
                        }
                    } else {
                        newItems.addAll(category.subcategories[""]!!)
                    }
                }
            }
        }

        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Category -> TYPE_CATEGORY
            is Subcategory -> TYPE_SUBCATEGORY
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
        val category = items[position] as Category
        holder.bind(category.name)

        holder.itemView.setOnClickListener {
            if (category.name in expandedCategories) {
                expandedCategories.remove(category.name)
                expandedSubcategories.removeAll { it.startsWith("${category.name}/") }
            } else {
                expandedCategories.add(category.name)
            }
            submitItems(items.filterIsInstance<Item>())
        }
    }

    private fun bindSubcategory(holder: SubcategoryViewHolder, position: Int) {
        val subcategory = items[position] as Subcategory
        holder.bind(subcategory.name)

        holder.itemView.setOnClickListener {
            val fullPath = "${subcategory.parentCategory}/${subcategory.name}"
            if (fullPath in expandedSubcategories) {
                expandedSubcategories.remove(fullPath)
            } else {
                expandedSubcategories.add(fullPath)
            }
            submitItems(items.filterIsInstance<Item>())
        }
    }

    private fun bindItem(holder: ItemViewHolder, position: Int) {
        val item = items[position] as Item
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tvTitle)
        fun bind(name: String) {
            title.text = name
        }
    }

    inner class SubcategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tvTitle)
        fun bind(name: String) {
            title.text = name
        }
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.item_title)
        private val category: TextView = view.findViewById(R.id.item_category)
        private val colorIndicator: View = view.findViewById(R.id.item_rarity_color)
        private val state: TextView = view.findViewById(R.id.item_state)
        private val icon: ImageView = view.findViewById(R.id.item_icon)

        fun bind(item: Item) {
            name.text = when (val name = item.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            category.text = item.category.split("/")[0]
            colorIndicator.setBackgroundColor(0xF0F)
            state.text = item.status.state
            Picasso.get().load(ApiClient.DATABASE_BASE_URL + item.iconPath).into(icon)
        }
    }
}

data class Category(
    val name: String,
    val subcategories: MutableMap<String, MutableList<Item>>
)

data class Subcategory(
    val parentCategory: String,
    val name: String
)