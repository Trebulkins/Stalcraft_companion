package com.example.stalcraft_companion.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.VersionInfo

@Database(entities = [Item::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun itemDao(): ItemDao

  companion object {
    @Volatile private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "items.db"
        )
          .allowMainThreadQueries()
          .build()
          .also { INSTANCE = it }
      }
    }
  }
}