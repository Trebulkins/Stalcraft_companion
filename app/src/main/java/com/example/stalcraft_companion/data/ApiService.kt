package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.InfoBlock
import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.ListingItem
import com.example.stalcraft_companion.data.modles.TranslationString
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("listing.json")
    suspend fun getItemsListing(): List<ListingItem>

    @GET("{itemPath}")
    suspend fun getItem(@Path("itemPath") itemPath: String): Item
}

object ApiClient {
    const val BASE_URL = "https://raw.githubusercontent.com/EXBO-Studio/stalcraft-database/main/ru/"

    val instance: ApiService by lazy {
        val gson = GsonBuilder()
            .registerTypeAdapter(TranslationString::class.java, TranslationStringAdapter())
            .registerTypeAdapter(InfoBlock::class.java, InfoBlocksObjectAdapter())
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}