package com.example.stalcraft_companion.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.stalcraft_companion.api.schemas.Item
import io.reactivex.Observable

@Dao
interface ItemsDao {

  @get:Query("SELECT * FROM items_table")
  val all: Observable<List<Item>>

  @Insert(onConflict = REPLACE)
  fun insert(movie: Item)

  @Query("DELETE FROM items_table WHERE id = :id")
  fun delete(id: String?)

  @Query("DELETE FROM items_table")
  fun deleteAll()

  @Update
  fun update(movie: Item)
}