package com.example.bangkitevent.di

import android.content.Context
import com.example.bangkitevent.data.remote.local.repository.EventRepo
import com.example.bangkitevent.data.remote.local.room.EventDatabase
import com.example.bangkitevent.data.remote.retrofit.ApiConfig
import com.example.bangkitevent.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepo {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        return EventRepo.getInstance(apiService, dao, appExecutors)
    }
}