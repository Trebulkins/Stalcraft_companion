package com.example.stalcraft_companion.data.modles

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "items")
@Parcelize
data class Item (
    @Expose @SerializedName("id") @PrimaryKey var id: String,
    @Expose @SerializedName("category") var category: String,
    @Expose @SerializedName("name") var name: TranslationString,
    @Expose @SerializedName("color") var color: String = "DEFAULT",
    @Expose @SerializedName("status") var status: StatusObject,
    @Expose @SerializedName("infoBlocks") var infoBlocks: List<InfoBlock>? = null,
): Parcelable {
    val subcategory: String get() = category.substringAfter('/', missingDelimiterValue = "")
}

@Parcelize
data class StatusObject (
    @Expose @SerializedName("state") var state: String
): Parcelable