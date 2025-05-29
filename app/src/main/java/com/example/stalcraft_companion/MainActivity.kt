package com.example.stalcraft_companion

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stalcraft_companion.api.schemas.ListingItem
import com.example.stalcraft_companion.fragments.DetailFragment
import com.example.stalcraft_companion.fragments.MainFragment

class MainActivity : AppCompatActivity(), MainFragment.OnItemSelectedListener {
    private var isLandscape = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance())
                .commit()
        }

        if (isLandscape) {
            // В альбомной ориентации сразу добавляем пустой фрагмент деталей справа
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(null))
                .commit()
        }
    }

    override fun onItemSelected(item: ListingItem) {
        if (isLandscape) {
            // В альбомной ориентации заменяем фрагмент справа
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, DetailFragment.newInstance(item))
                .commit()
        } else {
            // В портретной ориентации добавляем поверх текущего фрагмента
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailFragment.newInstance(item))
                .addToBackStack(null)
                .commit()
        }
    }
}