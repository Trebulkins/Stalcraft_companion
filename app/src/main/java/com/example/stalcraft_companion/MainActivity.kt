package com.example.stalcraft_companion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                val categoryGroups = t.groupBy { it.category }
                    .map { (category, categoryItems) ->
                        // Разделяем на элементы с подкатегориями и без
                        val (withSubcat, withoutSubcat) = categoryItems.partition { it.hasSubcategory }

                        val subcategories = withSubcat.groupBy { it.subcategory }
                            .map { (subcategory, subcategoryItems) ->
                                SubcategoryGroup(subcategory, subcategoryItems)
                            }

                        // Добавляем элементы без подкатегорий как отдельную "пустую" подкатегорию
                        val allSubcategories = if (withoutSubcat.isNotEmpty()) {
                            subcategories + SubcategoryGroup("", withoutSubcat)
                        } else {
                            subcategories
                        }

                        CategoryGroup(
                            categoryName = category,
                            subcategories = allSubcategories.sortedBy { it.subcategoryName }
                        )
                    }
                    .sortedBy { it.categoryName }

                adapter = ItemListingAdapter(this@MainActivity, categoryGroups) { item -> showItemDetails(item) }
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

    private fun showItemDetails(item: ListingItem) {
        // Показать детали элемента
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra("ITEM_DATA", item.data)
        }
        startActivity(intent)
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