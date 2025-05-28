package com.example.stalcraft_companion.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stalcraft_companion.api.schemas.Item

@Database(entities = [Item::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class LocalDatabase : RoomDatabase() {
  abstract fun itemsDao(): ItemsDao

  companion object {
    private val lock = Any()
    private const val DB_NAME = "items_database"
    private var INSTANCE: LocalDatabase? = null
    fun getInstance(application: Application): LocalDatabase {
      synchronized(lock) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(application, LocalDatabase::class.java, DB_NAME)
            .allowMainThreadQueries()
            .build()
        }
      }
      return INSTANCE!!
    }
  }
}