package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InfoBlocksObject(
    @SerializedName("type")
    @Expose
    var type: String,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("text")
    @Expose
    var text: String? = null,
)
