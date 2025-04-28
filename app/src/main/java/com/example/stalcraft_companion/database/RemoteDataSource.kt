package com.example.stalcraft_companion.database

import android.util.Log
import com.example.stalcraft_companion.api.RetrofitClient
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

private val TAG = "RemoteDataSource"

open class RemoteDataSource {
    fun searchResultsObservable(query: String): Observable<KinoResponse> {
        Log.d(TAG, "search/items")
        return RetrofitClient.itemsApi
            .searchMovie(RetrofitClient.APP_TOKEN)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}