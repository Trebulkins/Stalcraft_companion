package com.example.stalcraft_companion.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.stalcraft_companion.api.schemas.ListingItem
import io.reactivex.Observable

interface ListingDao {
    @get:Query("SELECT * FROM listing")
    val allListing: Observable<List<ListingItem>>

    @Insert(onConflict = REPLACE)
    fun insertListing(l: ListingItem)

    @Update
    fun updateListing(l: ListingItem)
}