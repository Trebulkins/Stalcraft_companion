package com.example.stalcraft_companion.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.stalcraft_companion.R
import com.example.stalcraft_companion.data.modles.Item
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), MainFragment.OnItemSelectedListener {
    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance())
                .commit()

            if (isLandscape) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.details_container, DetailsFragment.newInstance(null))
                    .commit()
            }
        }
    }

    override fun onItemSelected(item: Item) {
        if (isLandscape) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.details_container, DetailsFragment.newInstance(item))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, DetailsFragment.newInstance(item))
                .addToBackStack(null)
                .commit()
        }
    }
}