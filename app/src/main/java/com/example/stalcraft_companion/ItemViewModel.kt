package com.example.stalcraft_companion

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.database.ItemRepository
import com.example.stalcraft_companion.database.LocalDatabase
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    val allItems: LiveData<List<Item>>
    val isLoading = MutableLiveData(false)
    val error = MutableLiveData<String?>()

    init {
        val dao = LocalDatabase.getDatabase(application).itemDao()
        repository = ItemRepository(dao)
        allItems = repository.allItems.asLiveData()
    }

    fun refreshData() {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                repository.refreshData()
            } catch (e: Exception) {
                error.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }

    suspend fun getItemById(itemId: String): Item? = repository.getItemById(itemId)
}