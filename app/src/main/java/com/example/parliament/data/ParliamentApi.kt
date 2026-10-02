package com.example.parliament.data

import retrofit2.http.GET

interface ParliamentApi {

    @GET("~peterh/seating.json")
    suspend fun getMembers(): List<Member>
}