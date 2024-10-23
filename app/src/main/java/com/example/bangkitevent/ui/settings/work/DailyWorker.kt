package com.example.bangkitevent.ui.settings.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bangkitevent.R
import com.example.bangkitevent.data.remote.response.Event
import com.example.bangkitevent.data.remote.response.EventResponse
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.ui.settings.SettingPreferences
import com.example.bangkitevent.ui.settings.dataStore
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.DecimalFormat

class DailyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams)  {

    private var resultStatus: Result? = null


    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "rif channel"
    }

    override fun doWork(): Result {
        Log.d("DailyWorker", "Worker is running")
        val preferences = SettingPreferences.getInstance(applicationContext.dataStore)
        val isNotificationInactive = runBlocking { preferences.getNotificationSetting().first() }

        if (!isNotificationInactive) {
            runBlocking {
                val event = fetchActiveEvent()
                event?.let {
                    it.name?.let { name -> showNotification(name, "Dimulai pada : " + it.beginTime) }
                    Log.d("DailyWorker", "Success to show notification ${it.name}")
                }
            }
        }

        return Result.success()
    }

    private fun fetchActiveEvent(): Event? {
        var event: Event? = null
        Looper.prepare()
        val client = SyncHttpClient()
        val url = "https://event-api.dicoding.dev/events?active=-1&limit=1"

        client.get(url, object : AsyncHttpResponseHandler(Looper.getMainLooper()) {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                Log.d("DailyWorker", "Success to fetch active event")
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val jsonAdapter = moshi.adapter(EventResponse::class.java)
                val eventResponse = jsonAdapter.fromJson(String(responseBody))
                event = eventResponse?.listEvents?.firstOrNull()?.toEvent()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d("DailyWorker", "Failed to fetch active event: ${error.message}")
            }
        })
        return event
    }

    fun ListEventsItem.toEvent(): Event {
        return Event(
            summary = this.summary,
            mediaCover = this.mediaCover,
            registrants = this.registrants,
            imageLogo = this.imageLogo,
            link = this.link,
            description = this.description,
            ownerName = this.ownerName,
            cityName = this.cityName,
            quota = this.quota,
            name = this.name,
            id = this.id?.toInt(),
            beginTime = this.beginTime,
            endTime = this.endTime,
            category = this.category
        )
    }

    private fun showNotification(title: String, description: String?) {
//        Log.d("DailyWorker", "Showing notification with title: $title and description: $description")
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}