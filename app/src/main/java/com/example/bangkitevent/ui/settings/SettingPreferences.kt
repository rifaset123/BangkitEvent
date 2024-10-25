package com.example.bangkitevent.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    // Menyimpan Data ke Preferences DataStore
    private val THEME_KEY = booleanPreferencesKey("theme_setting")
    private val NOTIFICATION_KEY = booleanPreferencesKey("notification_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: true
        }
    }

    // notif
    fun getNotificationSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATION_KEY] ?: false
        }
    }

    @Suppress("unused")
    suspend fun saveNotificationSetting(isNotificationActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = isNotificationActive
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        // mengambil data dari dataStore dan melakukan perubahan data
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        // Singleton yaitu dapat menciptakan satu instance saja di dalam JVM, sehingga tidak memakan memori yang terbatas.
        @Volatile // keyword yang digunakan supaya nilai pada suatu variabel tidak dimasukkan ke dalam cache.
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}