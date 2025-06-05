package com.example.stalcraft_companion.api.schemas

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = "items")
@Parcelize
data class Item (
    @PrimaryKey
    @SerializedName("id")
    @Expose
    var id: String,

    @SerializedName("category")
    @Expose
    var category: String,

    @SerializedName("name")
    @Expose
    var name: TranslationString,

    @SerializedName("color")
    @Expose
    var color: String = "DEFAULT",

    @SerializedName("status")
    @Expose
    var status: StatusObject? = null,

    @SerializedName("infoBlocks")
    @Expose
    var infoBlocks: List<InfoBlocksObject>? = null,
): Parcelable {
    val subcategory: String get() = category.substringAfter('/', missingDelimiterValue = "")
}