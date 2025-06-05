package com.example.stalcraft_companion.database

import androidx.room.TypeConverter
import com.example.stalcraft_companion.api.schemas.InfoBlock
import com.example.stalcraft_companion.api.schemas.StatusObject
import com.example.stalcraft_companion.api.schemas.TranslationString
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class TypeConverter {
  private val gson = Gson()

  // TranslationString Converter
  @TypeConverter
  fun fromTranslationString(value: TranslationString): String {
    return when (value) {
      is TranslationString.Translation ->
        """{"type":"translation","lines":${Gson().toJson(value.lines)}}"""
      is TranslationString.Text ->
        """{"type":"text","text":"${value.text}"}"""
    }
  }

  @TypeConverter
  fun toTranslationString(value: String): TranslationString {
    return TranslationString.fromJson(value)
  }

  // InfoBlocksObject Converter
  @TypeConverter
  fun fromInfoBlocksList(value: List<InfoBlock>): String {
    return Gson().toJson(value.map { block ->
      when (block) {
        is InfoBlock.TextBlock ->
          """{
            "type":"text",
            "title":${fromTranslationString(block.title)},
            "text":${fromTranslationString(block.text)}
            }""".trimIndent()
        is InfoBlock.DamageBlock ->
          """{
            "type":"damage",
            "startDamage":${block.startDamage},
            "damageDecreaseStart":${block.damageDecreaseStart},
            "endDamage":${block.endDamage},
            "damageDecreaseEnd":${block.damageDecreaseEnd},
            "maxDistance":${block.maxDistance},
            }""".trimIndent()
        is InfoBlock.KeyValueBlock ->
          """{
            "type":"key-value",
            "key":${fromTranslationString(block.key)},
            "value":${fromTranslationString(block.value)}
            }""".trimIndent()
        is InfoBlock.ListBlock ->
          TODO()
        is InfoBlock.NumericBlock ->
          TODO()
      }
    })
  }

  @TypeConverter
  fun toInfoBlocksList(value: String): List<InfoBlock> {
    return JSONArray(value).map { json: JSONObject ->
      InfoBlock.fromJson(json.toString())
    }
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