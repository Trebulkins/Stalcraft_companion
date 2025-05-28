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
)

data class StatusObject(
    @SerializedName("state")
    @Expose
    var state: String
)

data class CategoryGroup(
    val categoryName: String,
    val items: List<ListingItem>,
    var isExpanded: Boolean = false
)