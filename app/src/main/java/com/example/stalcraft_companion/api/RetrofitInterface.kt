package com.example.stalcraft_companion.api

import com.example.stalcraft_companion.api.schemas.Region
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitInterface {
    @GET("regions")
    fun getAllItems(@Header("Authorization: Bearer ") api_key: String): Observable<Region>
}