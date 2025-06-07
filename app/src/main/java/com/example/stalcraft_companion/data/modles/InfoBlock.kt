package com.example.stalcraft_companion.data.modles

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class InfoBlock : Parcelable {
    abstract val type: String?

    @Parcelize
    data class TextBlock(
        @Expose @SerializedName("type") override val type: String = "text",
        @Expose @SerializedName("title") val title: TranslationString?,
        @Expose @SerializedName("text") val text: TranslationString
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class DamageBlock(
        @Expose @SerializedName("type") override val type: String = "damage",
        @Expose @SerializedName("startDamage") val startDamage: Float,
        @Expose @SerializedName("damageDecreaseStart") val damageDecreaseStart: Float,
        @Expose @SerializedName("endDamage") val endDamage: Float,
        @Expose @SerializedName("damageDecreaseEnd") val damageDecreaseEnd: Float,
        @Expose @SerializedName("maxDistance") val maxDistance: Float
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class ListBlock(
        @Expose @SerializedName("type") override val type: String = "list",
        @Expose @SerializedName("title") val title: TranslationString,
        @Expose @SerializedName("elements") val elements: List<InfoBlock>?
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class NumericBlock(
        @Expose @SerializedName("type") override val type: String = "numeric",
        @Expose @SerializedName("name") val name: TranslationString,
        @Expose @SerializedName("value") val value: Float,
        @Expose @SerializedName("formatted") val formatted: FormattedObject
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class KeyValueBlock(
        @Expose @SerializedName("type") override val type: String = "key-value",
        @Expose @SerializedName("key") val key: TranslationString,
        @Expose @SerializedName("value") val value: TranslationString
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class RangeBlock(
        @Expose @SerializedName("type") override val type: String = "range",
        @Expose @SerializedName("name") val name: TranslationString,
        @Expose @SerializedName("min") val min: Float,
        @Expose @SerializedName("max") val max: Float
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class UsageBlock(
        @Expose @SerializedName("type") override val type: String = "usage",
        @Expose @SerializedName("name") val name: TranslationString
    ) : InfoBlock(), Parcelable

    @Parcelize
    data class ItemBlock(
        @Expose @SerializedName("type") override val type: String = "item",
        @Expose @SerializedName("name") val name: TranslationString
    ) : InfoBlock(), Parcelable
}

@Parcelize
data class FormattedObject(
    @Expose @SerializedName("value") val value: TranslationLines,
    @Expose @SerializedName("valueColor") val valueColor: String
) : Parcelable