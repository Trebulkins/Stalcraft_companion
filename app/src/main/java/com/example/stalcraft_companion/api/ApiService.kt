package com.example.stalcraft_companion.api

import com.example.stalcraft_companion.api.schemas.Item
import com.example.stalcraft_companion.api.schemas.ListingItem
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("listing.json")
    suspend fun getItemListings(): List<ListingItem>

    @GET("{itemPath}")
    suspend fun getItem(@Path("itemPath") itemPath: String): Item
}

object ApiClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/EXBO-Studio/stalcraft-database/main/ru/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}