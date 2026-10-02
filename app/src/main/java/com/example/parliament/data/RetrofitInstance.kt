package com.example.parliament.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: ParliamentApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://users.metropolia.fi/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ParliamentApi::class.java)
    }
}