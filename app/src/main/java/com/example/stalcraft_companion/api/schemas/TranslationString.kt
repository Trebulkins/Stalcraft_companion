package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TranslationString(
    @SerializedName("type")
    @Expose
    var type: String,

    @SerializedName("key")
    @Expose
    var key: String,

    @SerializedName("args")
    @Expose
    var args: List<ValueObject>? = null,

    @SerializedName("lines")
    @Expose
    var lines: TranslationStringObj,
)