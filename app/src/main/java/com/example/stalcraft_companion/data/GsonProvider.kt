package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {
    private fun getGsonBuilder(): GsonBuilder {
        return GsonBuilder()
            .registerTypeAdapter(InfoBlock::class.java, InfoBlockDeserializer())
            .registerTypeAdapter(TranslationString::class.java, TranslationStringDeserializer())
    }
    val instance: Gson by lazy { getGsonBuilder().create() }
}