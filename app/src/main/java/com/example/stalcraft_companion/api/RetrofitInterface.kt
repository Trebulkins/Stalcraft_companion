package com.example.stalcraft_companion.api

import com.example.stalcraft_companion.api.responses.ListingResponse
import com.example.stalcraft_companion.api.schemas.GithubResponse
import com.example.stalcraft_companion.api.schemas.Item
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {
    @GET("listing.json")
    fun getItemsListing(): Observable<ListingResponse>

    @GET("{path}")
    fun goToPath(@Path("path") path: String): Observable<GithubResponse>

    @GET("{path}")
    fun getItem(@Path("path") path: String): Item
}