package com.example.stalcraft_companion.data

import androidx.room.TypeConverter
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.StatusObject
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {
  private val gson = Gson()

  @TypeConverter
  fun translationStringToJson(value: TranslationString): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun jsonToTranslationString(value: String): TranslationString {
    return gson.fromJson(value, TranslationString::class.java)
  }

  @TypeConverter
  fun infoBlocksListToJson(value: List<InfoBlock>): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun jsonToInfoBlocksList(value: String): List<InfoBlock> {
    val type = object : TypeToken<List<InfoBlock>>() {}.type
    return gson.fromJson(value, type) ?: emptyList()
  }

  @TypeConverter
  fun statusToJson(value: StatusObject): String {
    return gson.toJson(value)
  }

  @TypeConverter
  fun jsonToStatus(value: String): StatusObject {
    val type = object : TypeToken<List<InfoBlock>>() {}.type
    return gson.fromJson(value, type)
  }
}