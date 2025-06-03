package com.example.stalcraft_companion.database

import com.example.stalcraft_companion.api.ApiClient
import com.example.stalcraft_companion.api.schemas.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun refreshData() {
        try {
            val apiService = ApiClient.instance
            // Загрузка listing.json
            val itemListings = apiService.getItemsListing()

            // Параллельная загрузка всех items
            val items = itemListings.map { listing ->
                async {
                    try {
                        apiService.getItem(listing.data)
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()

            // Сохранение в базу данных
            itemDao.clearAll()
            itemDao.insertAll(items)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getItemById(itemId: String): Item? {
        return itemDao.getItemById(itemId)
    }
}