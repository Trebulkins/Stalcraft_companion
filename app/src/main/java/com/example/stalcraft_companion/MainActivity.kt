package com.example.stalcraft_companion

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.disposables.CompositeDisposable

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var dataSource: LocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        dataSource = LocalDataSource(application)
        getMyMoviesList()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private fun setupViews() {
        mainRecyclerView = findViewById(R.id.mainrecyclerview)
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}