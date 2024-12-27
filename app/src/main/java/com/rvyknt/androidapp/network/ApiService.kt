package com.rvyknt.androidapp.network

import com.rvyknt.androidapp.network.models.ResponseModel
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("auth/authenticateUser")
    fun getData(@Query("param") param: String?): Call<ResponseModel>



    @POST(ApiEndpoints.AUTHENTICATE_USER)
    fun postLogin(
        @Header("deviceId") deviceId: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: Map<String, String>
    ): Call<ResponseModel>
}