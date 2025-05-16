package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TranslationString(
    @SerializedName("type")
    @Expose
    var type: String,

    @SerializedName("text")
    @Expose
    var text: String? = null,

    @SerializedName("key")
    @Expose
    var key: String? = null,

    @SerializedName("args")
    @Expose
    var args: List<ValueObject>? = null,

    @SerializedName("lines")
    @Expose
    var lines: List<ValueObject>? = null,
)