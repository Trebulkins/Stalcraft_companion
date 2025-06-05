package com.example.stalcraft_companion.data

import android.util.Log
import com.example.stalcraft_companion.data.modles.Item
import kotlinx.coroutines.flow.Flow

private const val TAG = "ItemRepository"
class ItemRepository(private val itemDao: ItemDao) {
    fun getAllItems(): Flow<List<Item>> = itemDao.getAllItems()

    suspend fun refreshData(apiService: ApiService) {
        try {
            val items = apiService.getItemsListing().map { apiService.getItem(it.data) }
            itemDao.insertAll(items)
        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to parse: ${e.message}")
            throw e
        }
    }

    fun getItemById(id: String): Item? {
        return itemDao.getItemById(id)
    }
}