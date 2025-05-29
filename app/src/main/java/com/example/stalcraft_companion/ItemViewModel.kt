package com.example.stalcraft_companion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.database.ItemRepository
import com.example.stalcraft_companion.database.LocalDatabase

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    private val allItems: LiveData<List<Item>>
    private val isLoading = MutableLiveData(false)
    private val errorMessage = MutableLiveData<String?>()

    init {
        val dao = LocalDatabase.getDatabase(application).itemsDao()
        repository = ItemRepository(dao)
        allItems = repository.allItems.asLiveData()
    }

    fun refreshData() {
        viewModelScope.launch {
            isLoading.postValue(true)
            errorMessage.postValue(null)
            try {
                repository.refreshData(ApiClient.instance)
            } catch (e: Exception) {
                errorMessage.postValue("Failed to load data: ${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }
}