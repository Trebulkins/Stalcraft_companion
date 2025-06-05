package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.Item
import kotlinx.coroutines.flow.Flow

private const val TAG = "ItemRepository"
class ItemRepository(private val itemDao: ItemDao) {
    fun getAllItems(): Flow<List<Item>> = itemDao.getAllItems()

    suspend fun refreshData(apiService: ApiService) {
        val items = apiService.getItemsListing().map { apiService.getItem(it.data) }
        itemDao.insertAll(items)
    }

    fun getItemById(id: String): Item? {
        return itemDao.getItemById(id)
    }
}