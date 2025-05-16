package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item (
    @SerializedName("id")
    @Expose
    var id: String? = null,

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
    var status: ValueObject? = null,

    @SerializedName("infoBlocks")
    @Expose
    var infoBlocks: List<InfoBlocksObject>? = null,
)