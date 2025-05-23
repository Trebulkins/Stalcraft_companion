package com.example.stalcraft_companion.api.schemas

import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListingItem(
    @SerializedName("data")
    @Expose
    var data: String,

    @PrimaryKey
    @Expose
    var id: String = data.substringAfterLast('/').dropLast(5),

    @SerializedName("icon")
    @Expose
    var icon: String? = null,

    @SerializedName("name")
    @Expose
    var name: TranslationString,

    @SerializedName("color")
    @Expose
    var color: String? = "DEFAULT",

    @SerializedName("status")
    @Expose
    var status: StatusObject? = null,
)
