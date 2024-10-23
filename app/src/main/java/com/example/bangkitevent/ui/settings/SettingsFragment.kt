package com.example.bangkitevent.ui.settings

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import androidx.work.Constraints
import androidx.work.Data.Builder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.bangkitevent.R
import com.example.bangkitevent.ui.ViewModelFactory
import com.example.bangkitevent.ui.settings.work.DailyWorker
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    private val workManager: WorkManager by lazy { WorkManager.getInstance(requireActivity()) }
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val settingsViewModel: SettingsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireActivity(), "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
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

        // work
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        periodicWorkRequest = PeriodicWorkRequestBuilder<DailyWorker>(1, TimeUnit.DAYS)
            .build()

//        workManager.enqueueUniquePeriodicWork(
//            "DailyWorker",
//            ExistingPeriodicWorkPolicy.REPLACE,
//            periodicWorkRequest
//        )

        val dailyReminder = findPreference<SwitchPreferenceCompat>("reminder")
        dailyReminder?.setOnPreferenceChangeListener { _, newValue ->
            val isReminderActive = newValue as Boolean
            if (isReminderActive) {
                startPeriodicTask()
                Toast.makeText(requireActivity(), "Daily Reminder is now activated", Toast.LENGTH_SHORT).show()
            } else {
                cancelPeriodicTask()
            }
            Log.d("SettingsFragment", "onPreferenceChange: $isReminderActive")
            true
        }
    }

    // hanya untuk testing bahwa notifikasi berjalan
    private fun startOneTimeTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(DailyWorker::class.java)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(oneTimeWorkRequest)
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(DailyWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(periodicWorkRequest)
    }

    private fun cancelPeriodicTask() {
        workManager.cancelWorkById(periodicWorkRequest.id)
    }
}