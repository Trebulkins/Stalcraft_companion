package com.example.stalcraft_companion

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.adapters.ItemListingAdapter
import com.example.stalcraft_companion.api.schemas.ListingItem
import com.example.stalcraft_companion.database.LocalDataSource
import com.example.stalcraft_companion.database.RemoteDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var localDataSource: LocalDataSource
    private lateinit var adapter: ItemListingAdapter
    private val dataSource = RemoteDataSource()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        localDataSource = LocalDataSource(application)
        getItemsListing()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    private val itemsObservable: Observable<List<ListingItem>> = dataSource.listingObservable()
    private val observer: DisposableObserver<List<ListingItem>>
        get() = object : DisposableObserver<List<ListingItem>>() {
            override fun onNext(t: List<ListingItem>) {
                adapter = ItemListingAdapter(t, this@MainActivity)
                mainRecyclerView.adapter = adapter
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "Error $e")
                e.printStackTrace()
                displayError("Error fetching items list")
            }

            override fun onComplete() {
                Log.d(TAG, "Completed")
            }
        }

    private fun getItemsListing() {
        val itemsDisposable = itemsObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(observer)
        compositeDisposable.add(itemsDisposable)
    }

    private fun setupViews() {
        mainRecyclerView = findViewById(R.id.mainrecyclerview)
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showToast(str: String) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_LONG).show()
    }

    fun displayError(e: String) {
        showToast(e)
    }
}