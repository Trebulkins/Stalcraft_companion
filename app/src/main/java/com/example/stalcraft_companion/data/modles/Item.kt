package com.example.stalcraft_companion.data.modles

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "items")
data class Item (
    @Expose @SerializedName("id") @PrimaryKey var id: String,
    @Expose @SerializedName("name") var name: TranslationString,
    @Expose @SerializedName("category") var category: String,
    @Expose @SerializedName("color") var color: String = "DEFAULT",
    @Expose @SerializedName("status") var status: StatusObject,
    @Expose @SerializedName("infoBlocks") var infoBlocks: List<InfoBlock>?
): Parcelable {
    val iconPath get() = "icons/$category/$id.png"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Item
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}

@Parcelize
data class StatusObject (
    @Expose @SerializedName("state") var state: String
): Parcelable