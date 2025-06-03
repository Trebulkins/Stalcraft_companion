package com.example.stalcraft_companion.api.schemas

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items")
data class Item (
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("category")
    @Expose
    var category: String,

    @SerializedName("name")
    @Expose
    var name: TranslationString,

    @SerializedName("color")
    @Expose
    var color: String = "DEFAULT",

    @SerializedName("status")
    @Expose
    var status: StatusObject? = null,

    @SerializedName("infoBlocks")
    @Expose
    var infoBlocks: List<InfoBlocksObject>? = null,
) {
    val subcategory: String get() = category.substringAfter('/', missingDelimiterValue = "")
}