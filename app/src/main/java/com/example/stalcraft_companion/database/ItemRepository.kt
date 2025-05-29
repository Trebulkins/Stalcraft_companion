package com.example.stalcraft_companion.database

import com.example.stalcraft_companion.api.ApiService
import com.example.stalcraft_companion.api.schemas.Item
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {
    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    suspend fun refreshData(apiService: ApiService) {
        try {
            // 1. Загружаем список ItemListing
            val itemListings = apiService.getItemListings()

            // 2. Извлекаем пути к файлам Item
            val itemPaths = itemListings.map { it.data }

            // 3. Загружаем все Item по отдельности
            val items = itemPaths.mapNotNull { path ->
                try {
                    apiService.getItem(path)
                } catch (e: Exception) {
                    null // Пропускаем неудачные загрузки
                }
            }

            // 4. Сохраняем в базу данных
            itemDao.clearAll()
            itemDao.insertAll(items)
        } catch (e: Exception) {
            // Обработка ошибок
        }
    }
}