package com.example.stalcraft_companion.database

import android.app.Application
import com.example.stalcraft_companion.api.schemas.Item
import io.reactivex.Observable
import kotlin.concurrent.thread


open class LocalDataSource(application: Application) {
  private val dao: ItemsDao
  open val allItems: Observable<List<Item>>

  init {
    val db = LocalDatabase.getInstance(application)
    dao = db.itemsDao()
    allItems = dao.all
  }

  fun insert(item: Item) {
    thread {
      dao.insert(item)
    }
  }

  fun delete(item: Item) {
    thread {
      dao.delete(item.id)
    }
  }

  fun update(item: Item) {
    thread {
      dao.update(item)
    }
  }

}