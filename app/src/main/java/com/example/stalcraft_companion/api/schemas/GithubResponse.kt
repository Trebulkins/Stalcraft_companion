package com.example.stalcraft_companion.api.schemas

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("path")
    @Expose
    var path: String? = null,
)
