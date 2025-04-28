package com.example.stalcraft_companion.api.responses

import com.example.stalcraft_companion.api.schemas.ListingItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListingResponse {
    @SerializedName("items")
    @Expose
    var items: ListingItem? = null
}