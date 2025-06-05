package com.example.stalcraft_companion.api.schemas

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TranslationLines(
    @SerializedName("ru") val ru: String? = null,
    @SerializedName("eu") val eu: String? = null,
    @SerializedName("es") val es: String? = null
) : Parcelable

@Parcelize
data class FormattedObject(
    val value: TranslationLines
) : Parcelable