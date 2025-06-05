package com.example.stalcraft_companion.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainFragment.OnItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ItemViewModel
    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        viewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

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
}