package com.example.stalcraft_companion.api.schemas

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "listing")
data class ListingItem(
    @SerializedName("name")
    @Expose
    var name: TranslationString,

    @SerializedName("data")
    @Expose
    var data: String,

    @SerializedName("icon")
    @Expose
    var icon: String? = null,

    @SerializedName("color")
    @Expose
    var color: String,

    @SerializedName("status")
    @Expose
    var status: StatusObject
) {
    val id: String get() = data.split('/')[data.split('/').lastIndex].removeSuffix(".json")
    val category: String get() = if (data.split('/')[2] != "misc") data.split('/')[2] else "other"
    val hasSubcategory: Boolean get() = data.split('/').size > 4 // Есть подкатегория
    val subcategory: String get() = if (hasSubcategory && category != "other") data.split('/')[3] else ""
}

data class CategoryGroup(
    val categoryName: String,
    val subcategories: List<SubcategoryGroup>,
    var isExpanded: Boolean = false,
    val itemIds: List<String>  // Изменено на List<String>
) {
    fun hasSubcategories(): Boolean = subcategories.any { it.subcategoryName.isNotEmpty() }
}

data class SubcategoryGroup(
    val subcategoryName: String,
    var isExpanded: Boolean = false,
    val itemIds: List<String>  // Изменено на List<String>
)