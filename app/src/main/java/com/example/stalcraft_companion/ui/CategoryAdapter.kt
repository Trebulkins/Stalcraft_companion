package com.example.stalcraft_companion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.modles.CategoryGroup
import com.example.stalcraft_companion.data.modles.Item

class CategoryAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<CategoryGroup, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class Category(view: View) : ViewHolder(view)
        class Subcategory(view: View) : ViewHolder(view)
        class Item(view: View, val onClick: (Item) -> Unit) : ViewHolder(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> ViewHolder.Category(
                LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
            )
            TYPE_SUBCATEGORY -> ViewHolder.Subcategory(
                LayoutInflater.from(parent.context).inflate(R.layout.item_subcategory, parent, false)
            )
            else -> ViewHolder.Item(
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false),
                onItemClick
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.Category -> bindCategory(holder, position)
            is ViewHolder.Subcategory -> bindSubcategory(holder, position)
            is ViewHolder.Item -> bindItem(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Логика определения типа элемента
    }

    private fun bindCategory(holder: ViewHolder.Category, position: Int) {
        val category = getCategory(position)
        holder.itemView.findViewById<TextView>(R.id.tvTitle).text = category.name
    }

    private fun bindSubcategory(holder: CategoryAdapter.ViewHolder.Subcategory, position: Int) {
        val subcategory = getCategory(position)
        holder.itemView.findViewById<TextView>(R.id.tvTitle).text = subcategory.name
    }

    private fun bindItem(holder: CategoryAdapter.ViewHolder.Item, position: Int) {

    }

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_SUBCATEGORY = 1
        const val TYPE_ITEM = 2
    }
}