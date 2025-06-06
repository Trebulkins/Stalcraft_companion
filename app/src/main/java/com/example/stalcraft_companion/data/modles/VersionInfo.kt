package com.example.stalcraft_companion.data.modles

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "version")
@Parcelize
data class VersionInfo(
    @Expose @SerializedName("updated_at") val updatedAt: String
) : Parcelable
