package com.example.stalcraft_companion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stalcraft_companion.api.ApiClient
import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.database.ItemRepository
import com.example.stalcraft_companion.database.LocalDatabase

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    val allItems: LiveData<List<Item>>
    val isLoading = MutableLiveData(false)
    val errorMessage = MutableLiveData<String?>()

    init {
        val dao = LocalDatabase.getDatabase(application).itemDao()
        repository = ItemRepository(dao)
        allItems = repository.allItems.asLiveData()
    }

    fun refreshData() {
        viewModelScope.launch {
            isLoading.postValue(true)
            errorMessage.postValue(null)
            try {
                repository.refreshData()
            } catch (e: Exception) {
                errorMessage.postValue("Ошибка загрузки: ${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    suspend fun getItemById(itemId: String): Item? {
        return repository.getItemById(itemId)
    }
}