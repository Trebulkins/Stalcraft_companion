package com.example.stalcraft_companion.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.data.modles.CategoryGroup
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.TranslationString
import com.example.stalcraft_companion.databinding.ItemCategoryBinding
import com.example.stalcraft_companion.databinding.ItemLayoutBinding
import com.example.stalcraft_companion.databinding.ItemSubcategoryBinding

class CategoryAdapter(
    private val onItemClick: (Item) -> Unit
) : ListAdapter<CategoryGroup, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class Cat(val binding: ItemCategoryBinding) : ViewHolder(binding.root)
        class Subcat(val binding: ItemSubcategoryBinding) : ViewHolder(binding.root)
        class I(val binding: ItemLayoutBinding) : ViewHolder(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_CATEGORY -> ViewHolder.Cat(
                ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_SUBCATEGORY -> ViewHolder.Subcat(
                ItemSubcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> ViewHolder.I(
                ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.Cat -> bindCategory(holder, getItem(position))
            is ViewHolder.Subcat -> bindSubcategory(holder, getItem(position))
            is ViewHolder.I -> bindItem(holder, getItem(position))
        }
    }

    private fun bindCategory(holder: ViewHolder.Cat, group: CategoryGroup) {
        holder.binding.tvTitle.text = group.categoryName
    }

    private fun bindSubcategory(holder: ViewHolder.Subcat, group: CategoryGroup) {
        holder.binding.tvTitle.text = group.subcategories.first().subcategoryName
    }

    private fun bindItem(holder: ViewHolder.I, group: CategoryGroup) {
        val item = group.subcategories.first().items.first()
        holder.binding.apply {
            itemTitle.text = when (val name = item.name) {
                is TranslationString.Text -> name.text
                is TranslationString.Translation -> name.lines.ru
            }
            itemCategory.text = item.category
            itemRarityColor.setBackgroundColor(Color.parseColor(item.color))
            itemState.text = item.status.state
            root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position).isCategory -> TYPE_CATEGORY
            getItem(position).isSubcategory -> TYPE_SUBCATEGORY
            else -> TYPE_ITEM
        }
    }

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_SUBCATEGORY = 1
        const val TYPE_ITEM = 2
    }
}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryGroup>() {
    override fun areItemsTheSame(oldItem: CategoryGroup, newItem: CategoryGroup): Boolean {
        return oldItem.categoryName == newItem.categoryName
    }

    override fun areContentsTheSame(oldItem: CategoryGroup, newItem: CategoryGroup): Boolean {
        return oldItem == newItem
    }
}