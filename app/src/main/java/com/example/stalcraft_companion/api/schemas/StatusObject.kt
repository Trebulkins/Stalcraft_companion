package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusObject(
    @SerializedName("state")
    @Expose
    var state: String? = null
)
