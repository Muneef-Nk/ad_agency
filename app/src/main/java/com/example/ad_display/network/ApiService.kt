package com.example.ad_display.network


import com.example.ad_display.model.AdResponse
import com.example.ad_display.model.LoginRequest
import com.example.ad_display.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse



    @GET("/api/get_ad/")
    suspend fun getAds(@Header("Authorization") token: String): Response<AdResponse>

}
