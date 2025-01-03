package com.rvyknt.androidapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Base URL for the API
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    // Initialize the Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Lazy initialization of the API interface
    val apiInterface: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}
