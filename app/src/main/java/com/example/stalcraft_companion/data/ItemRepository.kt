package com.example.stalcraft_companion.data

import android.content.Context
import android.util.Log
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

private const val TAG = "ItemRepository"
class ItemRepository(private val itemDao: ItemDao) {
    fun getAllItems(): Flow<List<Item>> = itemDao.getAllItems()

    suspend fun needsUpdate(gitHubService: GitHubService, context: Context): Boolean {
        val remoteInfo = gitHubService.getUpdates()
        val localUpdate = Prefs.getLastUpdate(context)
        return localUpdate == null || remoteInfo.updatedAt > localUpdate
    }

    suspend fun refreshData(apiService: ApiService, gitHubService: GitHubService, context: Context) {
        try {
            val items = apiService.getItemsListing().map { apiService.getItem(it.data) }
            withContext(Dispatchers.IO) {
                itemDao.clearAll()
                itemDao.insertAll(items)
            }
            Prefs.setLastUpdate(context, gitHubService.getUpdates().updatedAt)
        } catch (e: Exception) {
            Log.e("API_ERROR", "Failed to parse: ${e.message}")
            throw e
        }
    }

    suspend fun getItemById(id: String): Item? {
        return withContext(Dispatchers.IO) {
            itemDao.getItemById(id)
        }
    }

    fun insertAll(items: MutableList<Item>) {
        itemDao.insertAll(items)
    }

    fun insertItem(item: Item) {
        itemDao.insert(item)
    }
}