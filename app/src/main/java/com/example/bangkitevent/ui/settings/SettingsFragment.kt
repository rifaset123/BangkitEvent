package com.example.bangkitevent.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.example.bangkitevent.R
import com.example.bangkitevent.ui.ViewModelFactory

class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = findPreference<SwitchPreferenceCompat>("theme")
        switchTheme?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkModeActive = newValue as Boolean
            settingsViewModel.saveThemeSetting(isDarkModeActive)
            Log.d("SettingsFragment", "onPreferenceChange: $isDarkModeActive")
            true
        }
        settingsViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}