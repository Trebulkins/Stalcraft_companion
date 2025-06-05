package com.example.stalcraft_companion.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.stalcraft_companion.data.ApiService
import com.example.stalcraft_companion.data.AppDatabase
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.ItemRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    val items: LiveData<List<Item>>
    val isLoading = MutableLiveData(false)
    val error = MutableLiveData<String?>()
    val isEmpty = MutableLiveData<Boolean>()

    // Новые поля для прогресса
    val totalItems = MutableLiveData<Int>(0)
    val loadedItems = MutableLiveData<Int>(0)
    val progressPercentage = MutableLiveData<Int>(0)

    init {
        val dao = AppDatabase.getInstance(application).itemDao()
        repository = ItemRepository(dao)
        items = repository.getAllItems().map { items ->
            isEmpty.postValue(items.isEmpty())
            items
        }.asLiveData()
    }

    fun refreshData(apiService: ApiService) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                // 1. Получаем список всех ItemListing
                val itemListings = apiService.getItemsListing()
                totalItems.value = itemListings.size

                // 2. Загружаем каждый Item по отдельности с прогрессом
                val items = mutableListOf<Item>()
                itemListings.forEachIndexed { index, listing ->
                    try {
                        val item = apiService.getItem(listing.data)
                        items.add(item)
                        loadedItems.value = index + 1
                        progressPercentage.value = ((index + 1) * 100 / itemListings.size)
                    } catch (e: Exception) {
                        Log.e("ItemLoad", "Error loading item ${listing.id}", e)
                    }
                }

                // 3. Сохраняем в базу
                repository.insertAll(items)
                progressPercentage.value = 100
            } catch (e: Exception) {
                error.value = "Ошибка загрузки: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getItemById(id: String): LiveData<Item?> {
        return liveData {
            emit(repository.getItemById(id))
        }
    }
}