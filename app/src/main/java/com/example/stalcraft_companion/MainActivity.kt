package com.example.stalcraft_companion

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.api.schemas.ListingResponse
import com.example.stalcraft_companion.database.RemoteDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var dataSource: RemoteDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    override fun onStart() {
        super.onStart()
        dataSource = RemoteDataSource()
        getItemsListing()
    }

    override fun onStop() {
        super.onStop()
    }

    private val myMoviesObservable: Observable<List<ListingResponse>>
        get() = dataSource.allItems
    private fun getItemsListing() {
        val itemsDisposable = itemsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)

        compositeDisposable.add(myMoviesDisposable)
    }

    private fun setupViews() {
        mainRecyclerView = findViewById(R.id.mainrecyclerview)
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    interface RecyclerItemListener {
        fun onItemClick(view: View, position: Int) {

        }
    }
}