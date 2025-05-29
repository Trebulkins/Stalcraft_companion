package com.example.stalcraft_companion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stalcraft_companion.api.schemas.ListingItem

@Database(entities = [ListingItem::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun listingDao(): ListingDao

  companion object {
    @Volatile
    private var INSTANCE: LocalDatabase? = null

    fun getDatabase(context: Context): LocalDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          LocalDatabase::class.java,
          "listing_database"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}