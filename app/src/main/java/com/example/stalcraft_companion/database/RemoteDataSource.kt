package com.example.stalcraft_companion.database

import android.util.Log
import com.example.stalcraft_companion.api.RetrofitClient
import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.api.schemas.ListingResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private val TAG = "RemoteDataSource"

open class RemoteDataSource {
    fun ListingObservable(): Observable<ListingResponse>? {
        Log.d(TAG, "GET: listing.json")
        return RetrofitClient.githubApi
            .getItemsListing()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}