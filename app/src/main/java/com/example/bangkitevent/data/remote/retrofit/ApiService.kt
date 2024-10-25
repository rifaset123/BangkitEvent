package com.example.bangkitevent.data.remote.retrofit

import com.example.bangkitevent.data.remote.response.EventIDResponse
import com.example.bangkitevent.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    fun getEvents(): Call<EventResponse>

    @Suppress("unused")
    // this for search all
    @GET("events?active=-1")
    fun searchEvents(@Query("q") query: String): Call<EventResponse>

    // search upcoming
    @GET("events?active=1")
    fun searchUpcoming(@Query("q") query: String): Call<EventResponse>

    // search finished
    @GET("events?active=0")
    fun searchFinished(@Query("q") query: String): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<EventIDResponse>
}