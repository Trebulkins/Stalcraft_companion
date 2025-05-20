package com.example.stalcraft_companion.api

import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.api.schemas.ListingResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {
    @GET("listing.json")
    fun getItemsListing(): Observable<List<ListingResponse>>

    @GET("{path}")
    fun getItem(@Path("path") path: String): Item
}