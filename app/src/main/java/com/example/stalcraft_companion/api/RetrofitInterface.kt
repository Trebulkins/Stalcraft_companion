package com.example.stalcraft_companion.api

import com.example.stalcraft_companion.api.responses.ListingResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface RetrofitInterface {
    @GET("listing.json")
    fun getItemsListing(): Observable<ListingResponse>
}