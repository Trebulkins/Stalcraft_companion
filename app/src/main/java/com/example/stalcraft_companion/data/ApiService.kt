package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.ListingItem
import com.example.stalcraft_companion.data.modles.VersionInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

interface GitHubService {
    @GET("repos/EXBO-Studio/stalcraft-database")
    suspend fun getUpdates(): VersionInfo
}

object ApiClient {
    const val UPDATE_URL = "https://api.github.com/"
    const val BASE_URL = "https://raw.githubusercontent.com/EXBO-Studio/stalcraft-database/main/ru/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonProvider.instance))
            .build()
            .create(ApiService::class.java)
    }

    val updInstance: GitHubService by lazy {
        Retrofit.Builder()
            .baseUrl(UPDATE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubService::class.java)
    }
}