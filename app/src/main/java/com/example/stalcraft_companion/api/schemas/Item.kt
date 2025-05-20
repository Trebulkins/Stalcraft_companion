package com.example.stalcraft_companion.api.schemas

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items_table")
data class Item (
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("category")
    @Expose
    var category: String? = null,

    @SerializedName("name")
    @Expose
    var name: TranslationString? = null,

    @SerializedName("color")
    @Expose
    var color: String? = "DEFAULT",

    @SerializedName("status")
    @Expose
    var status: StatusObject? = null,

    @SerializedName("infoBlocks")
    @Expose
    var infoBlocks: List<InfoBlocksObject>? = null,
)