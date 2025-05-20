package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListingResponse(
    @SerializedName("data")
    @Expose
    var data: String? = null,

    @SerializedName("icon")
    @Expose
    var icon: String? = null,

    @SerializedName("name")
    @Expose
    var name: TranslationString? = null,

    @SerializedName("color")
    @Expose
    var color: String? = null,

    @SerializedName("status")
    @Expose
    var status: StatusObject? = null,
)
