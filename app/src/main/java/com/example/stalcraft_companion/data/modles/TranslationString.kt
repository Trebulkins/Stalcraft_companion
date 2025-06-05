package com.example.stalcraft_companion.data.modles

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

sealed class TranslationString : Parcelable {
    @Parcelize
    data class Translation(
        @Expose @SerializedName("lines") val lines: TranslationLines
    ) : TranslationString()

    @Parcelize
    data class Text(
        @Expose @SerializedName("text") val text: String
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
    @Expose @SerializedName("ru") val ru: String? = null,
    @Expose @SerializedName("eu") val eu: String? = null,
    @Expose @SerializedName("es") val es: String? = null
) : Parcelable