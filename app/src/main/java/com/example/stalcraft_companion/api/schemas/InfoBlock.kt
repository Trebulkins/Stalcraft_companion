package com.example.stalcraft_companion.api.schemas

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

sealed class InfoBlock : Parcelable {
    abstract val type: String

    @Parcelize
    data class TextBlock(
        override val type: String = "text",
        val title: TranslationString,
        val text: TranslationString
    ) : InfoBlock()

    @Parcelize
    data class DamageBlock(
        override val type: String = "damage",
        val startDamage: Float,
        val damageDecreaseStart: Float,
        val endDamage: Float,
        val damageDecreaseEnd: Float,
        val maxDistance: Float
    ) : InfoBlock()

    @Parcelize
    data class ListBlock(
        override val type: String = "list",
        val title: TranslationString,
        val elements: List<InfoBlock>
    ) : InfoBlock()

    @Parcelize
    data class NumericBlock(
        override val type: String = "numeric",
        val name: TranslationString,
        val value: Float,
        val formatted: FormattedObject
    ) : InfoBlock()

    @Parcelize
    data class KeyValueBlock(
        override val type: String = "key-value",
        val key: TranslationString,
        val value: TranslationString
    ) : InfoBlock()

    companion object {
        fun fromJson(json: String): InfoBlock {
            val type = JSONObject(json).getString("type")
            return when (type) {
                "text" -> Gson().fromJson(json, TextBlock::class.java)
                "damage" -> Gson().fromJson(json, DamageBlock::class.java)
                "list" -> Gson().fromJson(json, ListBlock::class.java)
                "numeric" -> Gson().fromJson(json, NumericBlock::class.java)
                "key-value" -> Gson().fromJson(json, KeyValueBlock::class.java)
                else -> throw IllegalArgumentException("Unknown InfoBlock type")
            }
        }
    }
}