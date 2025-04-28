package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValueObject(
    @SerializedName("variable")
    @Expose
    var variable: String? = null,

    @SerializedName("value")
    @Expose
    var value: String? = null,
)
