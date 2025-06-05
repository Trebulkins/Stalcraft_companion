package com.example.stalcraft_companion.data.modles

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListingItem(
    @SerializedName("data") @Expose var data: String,
    @SerializedName("icon") @Expose var icon: String? = null,
    @SerializedName("name") @Expose var name: TranslationString,
    @SerializedName("color") @Expose var color: String,
    @SerializedName("status") @Expose var status: StatusObject
) {
    val id: String get() = data.split('/')[data.split('/').lastIndex].removeSuffix(".json")
    val category: String get() = if (data.split('/')[2] != "misc") data.split('/')[2] else "other"
    val hasSubcategory: Boolean get() = data.split('/').size > 4 // Есть подкатегория
    val subcategory: String get() = if (hasSubcategory && category != "other") data.split('/')[3] else ""
}

data class CategoryGroup(
    val categoryName: String,
    val subcategories: List<SubcategoryGroup>,
    val isCategory: Boolean = true,
    val isSubcategory: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CategoryGroup
        return categoryName == other.categoryName && subcategories == other.subcategories
    }

    override fun hashCode(): Int {
        return categoryName.hashCode()
    }
}

data class SubcategoryGroup(
    val subcategoryName: String,
    val items: List<Item>,
    val isCategory: Boolean = false,
    val isSubcategory: Boolean = true
)