package com.example.stalcraft_companion.data.modles

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

sealed class TranslationString : Parcelable {
    abstract val type: String?

    @Parcelize
    data class Translation(
        @Expose @SerializedName("type") override val type: String = "translation",
        @Expose @SerializedName("lines") val lines: TranslationLines
    ) : TranslationString()

    @Parcelize
    data class Text(
        @Expose @SerializedName("type") override val type: String = "text",
        @Expose @SerializedName("text") val text: String
    ) : TranslationString()
}

@Parcelize
data class TranslationLines(
    @Expose @SerializedName("ru") val ru: String? = null,
    @Expose @SerializedName("eu") val eu: String? = null,
    @Expose @SerializedName("es") val es: String? = null
) : Parcelable