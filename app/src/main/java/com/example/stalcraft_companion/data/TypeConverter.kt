package com.example.stalcraft_companion.data

import androidx.room.TypeConverter
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.StatusObject
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class TypeConverter {
  private val gson: Gson by lazy {
    GsonBuilder()
      .registerTypeAdapter(TranslationString::class.java, TranslationStringAdapter())
      .registerTypeAdapter(InfoBlock::class.java, InfoBlocksObjectAdapter())
      .create()
  }

  @TypeConverter
  fun fromTranslationString(value: TranslationString): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun toTranslationString(value: String): TranslationString {
    return gson.fromJson(value, TranslationString::class.java)
  }

  @TypeConverter
  fun fromInfoBlocksList(value: List<InfoBlock>): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun toInfoBlocksList(value: String): List<InfoBlock> {
    val type = object : TypeToken<List<InfoBlock>>() {}.type
    return gson.fromJson(value, type) ?: emptyList()
  }

  @TypeConverter
  fun toStatusObject(value: String): StatusObject {
    return gson.fromJson(value, StatusObject::class.java)
  }

  @TypeConverter
  fun fromStatusObject(value: StatusObject): String {
    return gson.toJson(value)
  }
}