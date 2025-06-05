package com.example.stalcraft_companion.data

import androidx.room.TypeConverter
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.StatusObject
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.reflect.TypeToken

class TypeConverter {
  @TypeConverter
  fun fromTranslationString(value: TranslationString): String {
    return GsonProvider.instance.toJson(value)
  }

  @TypeConverter
  fun toTranslationString(value: String): TranslationString {
    return GsonProvider.instance.fromJson(value, TranslationString::class.java)
  }

  @TypeConverter
  fun fromInfoBlocksList(value: List<InfoBlock>): String {
    return GsonProvider.instance.toJson(value)
  }

  @TypeConverter
  fun toInfoBlocksList(value: String): List<InfoBlock> {
    val type = object : TypeToken<List<InfoBlock>>() {}.type
    return GsonProvider.instance.fromJson(value, type) ?: emptyList()
  }

  @TypeConverter
  fun toStatusObject(value: String): StatusObject {
    return GsonProvider.instance.fromJson(value, StatusObject::class.java)
  }

  @TypeConverter
  fun fromStatusObject(value: StatusObject): String {
    return GsonProvider.instance.toJson(value)
  }
}