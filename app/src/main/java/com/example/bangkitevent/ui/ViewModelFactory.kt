package com.example.bangkitevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bangkitevent.data.remote.local.repository.EventRepo
import com.example.bangkitevent.di.Injection
import com.example.bangkitevent.ui.detail.DetailViewModel
import com.example.bangkitevent.ui.favorite.FavoriteViewModel
import com.example.bangkitevent.ui.finished.FinishedViewModel
import com.example.bangkitevent.ui.home.HomeViewModel
import com.example.bangkitevent.ui.settings.SettingPreferences
import com.example.bangkitevent.ui.settings.SettingsViewModel
import com.example.bangkitevent.utils.AppExecutors

class ViewModelFactory private constructor(private val eventRepo: EventRepo, private val settingPreferences: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> FinishedViewModel(eventRepo) as T
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> DetailViewModel(eventRepo) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(eventRepo) as T
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> favoriteViewModel as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> SettingsViewModel(settingPreferences) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    private val appExecutors = AppExecutors()
    private val favoriteViewModel = FavoriteViewModel(eventRepo, appExecutors)

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), Injection.provideSettingPreferences(context))
            }.also { instance = it }
    }
}