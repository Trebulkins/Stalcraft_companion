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