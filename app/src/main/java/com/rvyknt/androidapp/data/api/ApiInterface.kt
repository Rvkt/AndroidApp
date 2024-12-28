package com.rvyknt.androidapp.data.api

import com.rvyknt.androidapp.domain.UserModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    // Use suspend function for easier coroutine integration
    @GET("photos")
    fun getUsers(): Call<List<UserModel>>
}
