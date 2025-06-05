package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.FormattedObject
import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.StatusObject
import com.example.stalcraft_companion.data.modles.TranslationLines
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class TranslationStringAdapter : TypeAdapter<TranslationString>() {
    override fun write(out: JsonWriter, value: TranslationString) {
        when (value) {
            is TranslationString.Text -> {
                out.beginObject()
                out.name("type").value("text")
                out.name("text").value(value.text)
                out.endObject()
            }
            is TranslationString.Translation -> {
                out.beginObject()
                out.name("type").value("translation")
                out.name("lines").jsonValue(Gson().toJson(value.lines))
                out.endObject()
            }
        }
    }

    override fun read(reader: JsonReader): TranslationString {
        val json = JsonParser.parseReader(reader).asJsonObject
        return when (json.get("type").asString) {
            "text" -> TranslationString.Text(json.get("text").asString)
            "translation" -> TranslationString.Translation(
                Gson().fromJson(json.get("lines"), TranslationLines::class.java)
            )
            else -> throw IllegalArgumentException("Unknown TranslationString type")
        }
    }
}

class InfoBlocksObjectAdapter : TypeAdapter<InfoBlock>() {
    override fun write(out: JsonWriter, value: InfoBlock) {
        val json = when (value) {
            is InfoBlock.TextBlock -> JsonObject().apply {
                addProperty("type", "text")
                add("title", Gson().toJsonTree(value.text))
                add("text", Gson().toJsonTree(value.text))
            }
            is InfoBlock.DamageBlock -> JsonObject().apply {
                addProperty("type", "damage")
                addProperty("startDamage", value.startDamage)
                addProperty("damageDecreaseStart", value.damageDecreaseStart)
                addProperty("endDamage", value.endDamage)
                addProperty("damageDecreaseEnd", value.damageDecreaseEnd)
                addProperty("maxDistance", value.maxDistance)
            }
            is InfoBlock.ListBlock -> JsonObject().apply {
                addProperty("type", "list")
                add("title", Gson().toJsonTree(value.title))
                add("elements", Gson().toJsonTree(value.elements))
            }
            is InfoBlock.NumericBlock -> JsonObject().apply {
                addProperty("type", "numeric")
                add("name", Gson().toJsonTree(value.name))
                addProperty("value", value.value)
                add("formatted", Gson().toJsonTree(value.formatted))
            }
            is InfoBlock.KeyValueBlock -> JsonObject().apply {
                addProperty("type", "key-value")
                add("key", Gson().toJsonTree(value.key))
                add("value", Gson().toJsonTree(value.value))
            }
            is InfoBlock.RangeBlock -> JsonObject().apply {
                addProperty("type", "range")
                add("name", Gson().toJsonTree(value.name))
                addProperty("min", value.min)
                addProperty("max", value.max)
            }
            is InfoBlock.UsageBlock -> JsonObject().apply {
                addProperty("type", "usage")
                add("name", Gson().toJsonTree(value.name))
            }
            else -> throw IllegalArgumentException("Unsupported type")
        }
        out.jsonValue(Gson().toJson(json))
    }

    override fun read(reader: JsonReader): InfoBlock {
        val json = JsonParser.parseReader(reader).asJsonObject
        return when (json.get("type").asString) {
            "text" -> InfoBlock.TextBlock(
                type = "text",
                title = Gson().fromJson(json.get("title"), TranslationString::class.java),
                text = Gson().fromJson(json.get("text"), TranslationString::class.java)
            )
            "damage" -> InfoBlock.DamageBlock(
                type = "damage",
                startDamage = json.get("startDamage").asFloat,
                damageDecreaseStart = json.get("damageDecreaseStart").asFloat,
                endDamage = json.get("endDamage").asFloat,
                damageDecreaseEnd = json.get("damageDecreaseEnd").asFloat,
                maxDistance = json.get("maxDistance").asFloat
            )
            "list" -> InfoBlock.ListBlock(
                type = "list",
                title = Gson().fromJson(json.get("title"), TranslationString::class.java),
                elements = Gson().fromJson(json.get("elements"), object : TypeToken<StatusObject>() {}.type)
            )
            "numeric" -> InfoBlock.NumericBlock(
                type = "numeric",
                name = Gson().fromJson(json.get("name"), TranslationString::class.java),
                value = json.get("value").asFloat,
                formatted = Gson().fromJson(json.get("formatted"), FormattedObject::class.java)
            )
            "key-value" -> InfoBlock.KeyValueBlock(
                type = "key-value",
                key = Gson().fromJson(json.get("key"), TranslationString::class.java),
                value = Gson().fromJson(json.get("value"), TranslationString::class.java)
            )
            "range" -> InfoBlock.RangeBlock(
                type = "range",
                name = Gson().fromJson(json.get("name"), TranslationString::class.java),
                min = json.get("min").asFloat,
                max = json.get("max").asFloat
            )
            "usage" -> InfoBlock.UsageBlock(
                type = "range",
                name = Gson().fromJson(json.get("name"), TranslationString::class.java)
            )
            else -> throw IllegalArgumentException("Unknown InfoBlocksObject type")
        }
    }
}