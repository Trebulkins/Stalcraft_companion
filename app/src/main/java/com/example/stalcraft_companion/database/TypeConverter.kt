package com.example.stalcraft_companion.database

import androidx.room.TypeConverter
import com.example.stalcraft_companion.api.schemas.TranslationString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypeConverter {
  @TypeConverter
  fun translateStringToString(translateString: TranslationString?): String {
    val gson = Gson()
    return gson.toJson(translateString)
  }

  @TypeConverter
  fun stringToIntegerList(data: String?): MutableList<Int> {

    val gson = Gson()

    if (data.isNullOrEmpty() || data == "null") {
      return mutableListOf<Int>()
    }

    val listType = object : TypeToken<List<Int>>() {

    }.type

    return gson.fromJson(data, listType)
  }

  @TypeConverter
  fun integertListToString(someObjects: List<Int>?): String {
    val gson = Gson()

    return gson.toJson(someObjects)
  }

}