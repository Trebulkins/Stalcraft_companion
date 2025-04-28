package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,
)
