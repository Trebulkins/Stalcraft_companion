package com.example.stalcraft_companion.data

import androidx.room.TypeConverter
import com.example.stalcraft_companion.data.modles.FormattedObject
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.StatusObject
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.Gson
import org.json.JSONArray

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
  fun fromFormattedObject(value: FormattedObject): String {
    return """{"value":{"ru":${value.value.ru},"eu":${value.value.eu},"es":${value.value.es}}}"""
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
            }""".trimMargin()
        is InfoBlock.DamageBlock ->
          """{
            "type":"damage",
            "startDamage":${block.startDamage},
            "damageDecreaseStart":${block.damageDecreaseStart},
            "endDamage":${block.endDamage},
            "damageDecreaseEnd":${block.damageDecreaseEnd},
            "maxDistance":${block.maxDistance},
            }""".trimMargin()
        is InfoBlock.KeyValueBlock ->
          """{
            "type":"key-value",
            "key":${fromTranslationString(block.key)},
            "value":${fromTranslationString(block.value)}
            }""".trimMargin()
        is InfoBlock.ListBlock ->
          """{
            "type":"list",
            "title":${fromTranslationString(block.title)},
            "elements":${fromInfoBlocksList(block.elements)}
            }""".trimMargin()
        is InfoBlock.NumericBlock ->
          """{
            "type":"numeric",
            "name":${fromTranslationString(block.name)},
            "value":${block.value},
            "formatted":${fromFormattedObject(block.formatted)}
            }""".trimMargin()
        is InfoBlock.RangeBlock ->
          """{
            |"type":"range",
            |"name":${fromTranslationString(block.name)},
            |"min":${block.min},
            |"max":${block.max}
            |}""".trimMargin()
      }
    })
  }

  @TypeConverter
  fun toInfoBlocksList(value: String): List<InfoBlock> {
    return try {
      val jsonArray = JSONArray(value)
      List(jsonArray.length()) { index ->
        InfoBlock.fromJson(jsonArray.getJSONObject(index).toString())
      }
    } catch (e: Exception) {
      emptyList()
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