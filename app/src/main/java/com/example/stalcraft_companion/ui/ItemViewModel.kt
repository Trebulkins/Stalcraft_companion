package com.example.stalcraft_companion.ui

import android.app.Application
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
                repository.refreshData(apiService)
                error.value = null
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