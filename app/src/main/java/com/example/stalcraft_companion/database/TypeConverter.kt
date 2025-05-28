package com.example.stalcraft_companion.database

import androidx.room.TypeConverter
import com.example.stalcraft_companion.api.schemas.InfoBlocksObject
import com.example.stalcraft_companion.api.schemas.StatusObject
import com.example.stalcraft_companion.api.schemas.TranslationString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {
  private val gson = Gson()

  // TranslationString
  @TypeConverter
  fun getRussianText(translationString: TranslationString) = translationString.lines?.ru

  @TypeConverter
  fun stringToTranslationString(json: String): TranslationString {
    return gson.fromJson(json, TranslationString::class.java)
  }

  // List<InfoBlocksObject>
  @TypeConverter
  fun infoBlocksListToString(infoBlocks: List<InfoBlocksObject>): String {
    return gson.toJson(infoBlocks)
  }

  @TypeConverter
  fun stringToInfoBlocksList(json: String): List<InfoBlocksObject> {
    val type = object : TypeToken<List<InfoBlocksObject>>() {}.type
    return gson.fromJson(json, type)
  }

  // StatusObject
  @TypeConverter
  fun statusObjectToString(statusObject: StatusObject): String {
    return gson.toJson(statusObject)
  }

  @TypeConverter
  fun stringToStatusObject(json: String): StatusObject {
    return gson.fromJson(json, StatusObject::class.java)
  }
}