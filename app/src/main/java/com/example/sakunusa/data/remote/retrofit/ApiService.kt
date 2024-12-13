package com.example.sakunusa.data.remote.retrofit

import com.example.sakunusa.data.remote.response.AnomalyResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("/anomali/")
    suspend fun detectAnomaly(
        @Body requestBody: AnomalyRequestBody
    ): Response<AnomalyResponse>
}