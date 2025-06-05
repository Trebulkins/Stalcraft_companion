package com.example.stalcraft_companion.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.ApiClient
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), MainFragment.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ItemViewModel
    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this)[ItemViewModel::class.java]

        // Загрузка данных при старте
        loadInitialData()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance())
                .commit()

            if (isLandscape) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.details_container, DetailsFragment.newInstance(""))
                    .commit()
            }
        }
    }

    override fun onItemSelected(item: Item) {
        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.details_container, DetailsFragment.newInstance(item.id))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, DetailsFragment.newInstance(item.id))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadInitialData() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            viewModel.items.observe(this) { items ->
                if (items.isEmpty()) {
                    viewModel.refreshData(ApiClient.instance)
                }
                else {
                    Snackbar.make(binding.root, "База предметов уже установлена, обновлене не требуется", Snackbar.LENGTH_LONG).show()
                }
            }
        } else {
            Snackbar.make(binding.root, "Нет интернет-соединения", Snackbar.LENGTH_LONG).show()
        }
    }
}