package com.example.bangkitevent.data.retrofit

import com.example.bangkitevent.data.response.EventIDResponse
import com.example.bangkitevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    fun getEvents(): Call<EventResponse>

    @GET("events?active=-1")
    fun searchEvents(@Query("q") query: String): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<EventIDResponse>
}