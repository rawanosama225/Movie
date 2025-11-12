package com.example.myfinalproject.Model.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val api = Retrofit.Builder()
    .baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(ApiService::class.java)