package com.example.stalcraft_companion.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stalcraft_companion.api.schemas.Item

@Database(entities = [Item::class], version = 1)
@TypeConverters(TypeConverters::class)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun itemDao(): ItemDao

  companion object {
    @Volatile
    private var INSTANCE: LocalDatabase? = null

    fun getDatabase(context: Context): LocalDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          LocalDatabase::class.java,
          "items_database"
        ).build()
        INSTANCE = instance
        instance
      }
    }
  }
}