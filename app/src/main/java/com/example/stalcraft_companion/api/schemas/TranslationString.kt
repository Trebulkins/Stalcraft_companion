package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TranslationLines(
    @SerializedName("ru")
    @Expose
    var ru: String? = null,

    @SerializedName("en")
    @Expose
    var en: String? = null,

    @SerializedName("es")
    @Expose
    var es: String? = null,

)
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

    @SerializedName("lines")
    @Expose
    var lines: List<TranslationLines>? = null,
)