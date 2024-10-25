package com.example.bangkitevent.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getThemeSettings() = preferences.getThemeSetting().asLiveData()


    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }

}