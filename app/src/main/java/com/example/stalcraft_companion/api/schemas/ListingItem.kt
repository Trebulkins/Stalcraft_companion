package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListingItem(
    @SerializedName("data")
    @Expose
    var data: String,

    @SerializedName("icon")
    @Expose
    var icon: String? = null,

    @SerializedName("name")
    @Expose
    var name: TranslationString,

    @SerializedName("color")
    @Expose
    var color: String,

    @SerializedName("status")
    @Expose
    var status: StatusObject
) {
    val category: String get() = data.split('/')[2]
    val hasSubcategory: Boolean get() = data.split('/').size > 4 // Есть подкатегория
    val subcategory: String get() = if (hasSubcategory) data.split('/')[3] else ""
}

data class CategoryGroup(
    val categoryName: String,
    val subcategories: List<SubcategoryGroup>,
    var isExpanded: Boolean = false
) {
    fun hasSubcategories(): Boolean = subcategories.any { it.subcategoryName.isNotEmpty() }
    fun getAllItems(): List<ListingItem> = subcategories.flatMap { it.items }
}

data class SubcategoryGroup(
    val subcategoryName: String,
    val items: List<ListingItem>,
    var isExpanded: Boolean = false
)

data class StatusObject(
    @SerializedName("state")
    @Expose
    var state: String
)