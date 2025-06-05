package com.example.stalcraft_companion.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.ItemRepository
import com.example.stalcraft_companion.data.LocalDatabase
import kotlinx.coroutines.launch

class ItemViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ItemRepository
    val items: LiveData<List<Item>>

    init {
        val dao = LocalDatabase.getDatabase(application).itemDao()
        repository = ItemRepository(dao)
        items = repository.allItems.asLiveData()
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.refreshData()
        }
    }
}