package com.example.stalcraft_companion.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stalcraft_companion.DetailsActivity
import com.example.stalcraft_companion.R
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

private const val TAG = "MainFragment"
class MainFragment : Fragment() {
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var localDataSource: LocalDataSource
    private lateinit var adapter: ItemListingAdapter
    private val dataSource = RemoteDataSource()
    private val compositeDisposable = CompositeDisposable()

    companion object {
        fun newInstance() = MainFragment()
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: ListingItem)
    }
    private var listener: OnItemSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnItemSelectedListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val items = loadItems() // Ваш метод загрузки данных

        val categoryGroups = prepareCategoryGroups(items)
        adapter = ItemListingAdapter(categoryGroups) { item ->
            listener?.onItemSelected(item)
        }

        recyclerView.adapter = adapter
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

                adapter = ItemListingAdapter(categoryGroups) { item -> showItemDetails(item) }
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
        val intent = Intent(context, DetailsActivity::class.java).apply {
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

    private fun showToast(str: String) {
        Toast.makeText(this@MainActivity, str, Toast.LENGTH_LONG).show()
    }

    fun displayError(e: String) {
        showToast(e)
    }

    fun downloadListing() {
        val listing: Observable<List<ListingItem>> = dataSource.listingObservable()
        for (item: ListingItem in listing) {
            ItemsDao.insertListing(item)
        }
    }
}