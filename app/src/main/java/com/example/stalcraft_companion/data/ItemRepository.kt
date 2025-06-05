package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    fun getAllItems(): Flow<List<Item>> = itemDao.getAllItems()

    suspend fun refreshData(apiService: ApiService) {
        try {
            val listings = apiService.getItemsListing()
            val items = listings.map { apiService.getItem(it.data) }
            itemDao.clearAll()
            itemDao.insertAll(items)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getItemById(id: String): Item? {
        return itemDao.getItemById(id)
    }
}