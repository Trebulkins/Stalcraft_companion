package com.example.stalcraft_companion.database

import android.util.Log
import com.example.stalcraft_companion.api.RetrofitClient
import com.example.stalcraft_companion.api.schemas.ListingItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private const val TAG = "RemoteDataSource"

open class RemoteDataSource {
    fun listingObservable(): Observable<List<ListingItem>> {
        Log.d(TAG, "GET: listing.json")
        return RetrofitClient.githubApi
            .getItemsListing()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}