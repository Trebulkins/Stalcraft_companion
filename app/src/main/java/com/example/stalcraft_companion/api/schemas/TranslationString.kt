package com.example.stalcraft_companion.api.schemas

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

sealed class TranslationString : Parcelable {
    @Parcelize
    data class Translation(
        val lines: TranslationLines
    ) : TranslationString()

    @Parcelize
    data class Text(
        val text: String
    ) : TranslationString()

    companion object {
        fun fromJson(json: String): TranslationString {
            val type = JSONObject(json).getString("type")
            return when (type) {
                "translation" -> Gson().fromJson(json, Translation::class.java)
                "text" -> Gson().fromJson(json, Text::class.java)
                else -> throw IllegalArgumentException("Unknown TranslationString type")
            }
        }
    }
}

@Parcelize
data class TranslationLines(
    @SerializedName("ru") val ru: String? = null,
    @SerializedName("eu") val eu: String? = null,
    @SerializedName("es") val es: String? = null
) : Parcelable

@Parcelize
data class FormattedObject(
    @SerializedName("value") val value: TranslationLines
) : Parcelable