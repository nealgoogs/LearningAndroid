package com.example.hw_4

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("events.json")
    fun getEvents(
        @Query("apikey") apiKey: String,
        @Query("classificationName") classificationName: String,
        @Query("city") city: String
    ) : Call<TicketmasterResponse>

    @GET("events.json")
    fun getSortedEvents(
        @Query("apikey") apiKey: String,
        @Query("classificationName") classificationName: String,
        @Query("city") city: String,
        @Query("sort") sort: String
    ) : Call<TicketmasterResponse>

}
