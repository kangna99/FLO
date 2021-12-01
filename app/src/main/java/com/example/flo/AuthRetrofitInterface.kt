package com.example.flo

import com.example.flo.db.User
import retrofit2.Call
import retrofit2.http.*

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("/users/auto-login")
    fun autoLogin(@Header("jwt") jwt: String): Call<AuthResponse>
}