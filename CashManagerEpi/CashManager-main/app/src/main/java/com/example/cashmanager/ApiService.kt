package com.example.cashmanager

import com.example.cashmanager.models.User
import com.example.cashmanager.models.AllBank
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("getAllBank")
    suspend fun getAllBank(): Response<MutableList<AllBank>>

    @POST("user")
    suspend fun postUser(@Body post: User): Response<User>

}

