package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {
    private fun getGsonBuilder(): GsonBuilder {
        return GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(TranslationString::class.java, "type")
                    .registerSubtype(TranslationString.Text::class.java, "text")
                    .registerSubtype(TranslationString.Translation::class.java, "translation")
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(InfoBlock::class.java, "type")
                    .registerSubtype(InfoBlock.TextBlock::class.java, "text")
                    .registerSubtype(InfoBlock.DamageBlock::class.java, "damage")
                    .registerSubtype(InfoBlock.NumericBlock::class.java, "numeric")
                    .registerSubtype(InfoBlock.ListBlock::class.java, "list")
                    .registerSubtype(InfoBlock.KeyValueBlock::class.java, "key-value")
                    .registerSubtype(InfoBlock.RangeBlock::class.java, "range")
                    .registerSubtype(InfoBlock.UsageBlock::class.java, "usage")
                    .registerSubtype(InfoBlock.ItemBlock::class.java, "item")
            )
    }
    val instance: Gson by lazy { getGsonBuilder().create() }
}