package com.example.bangkitevent.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings() = preferences.getThemeSetting().asLiveData()


    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }

    // notif
    fun getNotificationSettings() = preferences.getNotificationSetting().asLiveData()

    fun saveNotificationSetting(isNotificationActive: Boolean) {
        viewModelScope.launch {
            preferences.saveNotificationSetting(isNotificationActive)
        }
    }

//    class Factory(private val preferences: SettingPreferences) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return SettingsViewModel(preferences) as T
//            }
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//    }
}