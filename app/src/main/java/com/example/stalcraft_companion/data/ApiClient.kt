package com.example.stalcraft_companion.data

import com.example.stalcraft_companion.data.modles.Item
import com.example.stalcraft_companion.data.modles.ListingItem
import com.example.stalcraft_companion.data.modles.VersionInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApiService {
    @GET("repos/EXBO-Studio/stalcraft-database")
    suspend fun getRepoInfo(): VersionInfo
}

interface DatabaseApiService {
    @GET("listing.json")
    suspend fun getItemListings(): List<ListingItem>

    @GET("{itemPath}")
    suspend fun getItem(@Path("itemPath") itemPath: String): Item
}

object ApiClient {
    private const val GITHUB_BASE_URL = "https://api.github.com/"
    const val DATABASE_BASE_URL = "https://raw.githubusercontent.com/EXBO-Studio/stalcraft-database/main/ru/"

    val githubApi: GitHubApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GITHUB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }

    val databaseApi: DatabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(DATABASE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonProvider.instance))
            .build()
            .create(DatabaseApiService::class.java)
    }
}