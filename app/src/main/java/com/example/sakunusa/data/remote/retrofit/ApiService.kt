package com.example.sakunusa.data.remote.retrofit

import com.example.sakunusa.data.local.entity.RecordEntity
import com.example.sakunusa.data.remote.response.RecordItem
import com.example.sakunusa.data.remote.response.RecordsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface ApiService {
    @GET("/records")
    fun getRecords(): Call<RecordsResponse>

    @GET("/records/{id}")
    fun getRecord(@Path("id") id: Int): Call<RecordItem>

    @POST("/records/new")
    fun addRecord(@Body record: RecordEntity): Call<RecordItem>

    @PUT("/records/edit/{id}")
    fun updateRecord(@Path("id") id: Int): Call<RecordItem>

    @DELETE("/records/{id}")
    fun deleteRecord(@Path("id") id: Int): Call<RecordsResponse>
}