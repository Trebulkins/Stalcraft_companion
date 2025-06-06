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

    suspend fun needsUpdate(context: Context): Boolean {
        return try {
            val remoteInfo = ApiClient.githubApi.getRepoInfo()
            val localUpdate = Prefs.getLastUpdate(context)
            localUpdate == null || remoteInfo.updatedAt > localUpdate
        } catch (e: Exception) {
            false
        }
    }

    suspend fun refreshData(context: Context, progressCallback: (Int, Int) -> Unit) {
        try {
            val listings = ApiClient.databaseApi.getItemListings()
            val totalItems = listings.size
            val items = mutableListOf<Item>()

            listings.forEachIndexed { index, listing ->
                val item = ApiClient.databaseApi.getItem(listing.data)
                items.add(item)
                progressCallback(index + 1, totalItems)
            }

            itemDao.clearAll()
            itemDao.insertAll(items)

            val repoInfo = ApiClient.githubApi.getRepoInfo()
            Prefs.setLastUpdate(context, repoInfo.updatedAt)
        } catch (e: Exception) {
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