package com.example.stalcraft_companion

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.adapters.CategoryAdapter
import com.example.stalcraft_companion.adapters.ItemListingAdapter
import com.example.stalcraft_companion.api.schemas.CategoryGroup
import com.example.stalcraft_companion.api.schemas.ListingItem
import com.example.stalcraft_companion.api.schemas.SubcategoryGroup
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
    private lateinit var adapter: CategoryAdapter
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
                val categoryGroups = t.groupBy { it.data.substringBeforeLast('/').substringAfter('/').substringAfter('/').substringBeforeLast('/') }
                    .map { (category, categoryItems) ->
                        val subcategoryGroups = categoryItems.groupBy { it.data.substringBeforeLast('/').substringAfter('/').substringAfter('/') }
                            .map { (subcategory, subcategoryItems) ->
                                SubcategoryGroup(subcategory, subcategoryItems)
                            }
                            .sortedBy { it.subcategoryName }

                        CategoryGroup(category, subcategoryGroups)
                    }
                    .sortedBy { it.categoryName }

                adapter = CategoryAdapter(categoryGroups) { _ -> }
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