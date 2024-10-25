package com.example.bangkitevent.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.bangkitevent.data.remote.local.repository.EventRepo
import com.example.bangkitevent.data.remote.local.room.EventDatabase
import com.example.bangkitevent.data.remote.retrofit.ApiConfig
import com.example.bangkitevent.ui.settings.SettingPreferences
import com.example.bangkitevent.ui.settings.dataStore
import com.example.bangkitevent.utils.AppExecutors
import androidx.datastore.preferences.core.Preferences


object Injection {
    fun provideRepository(context: Context): EventRepo {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        return EventRepo.getInstance(apiService, appExecutors, dao )
    }

    // settings
    fun provideSettingPreferences(context: Context): SettingPreferences {
        val dataStore: DataStore<Preferences> = context.dataStore
        return SettingPreferences.getInstance(dataStore)
    }
}