package com.example.stalcraft_companion

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.api.schemas.ListingItem
import com.example.stalcraft_companion.fragments.DetailFragment
import com.example.stalcraft_companion.fragments.MainFragment
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), MainFragment.OnItemSelectedListener {
    private lateinit var viewModel: ItemViewModel
    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        setupObservers()
        loadData()

        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(null))
                .commit()
        }
    }

    private fun setupObservers() {
        viewModel.allItems.observe(this) { items ->
            if (items.isNotEmpty()) {
                showMainFragment(items)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Управление индикатором загрузки
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadData() {
        viewModel.refreshData()
    }

    private fun showMainFragment(items: List<Item>) {
        val fragment = MainFragment.newInstance(ArrayList(items))
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun onItemSelected(itemId: String) {
        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(itemId))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailFragment.newInstance(itemId))
                .addToBackStack(null)
                .commit()
        }
    }
}