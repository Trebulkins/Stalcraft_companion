package com.example.stalcraft_companion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.stalcraft_companion.api.schemas.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
  @Insert(onConflict = REPLACE)
  suspend fun insertAll(items: List<Item>)

  @Query("SELECT * FROM items")
  fun getAllItems(): Flow<List<Item>>

  @Query("SELECT * FROM items WHERE id = :itemId")
  suspend fun getItemById(itemId: String): Item?

  @Query("DELETE FROM items")
  suspend fun clearAll()
}