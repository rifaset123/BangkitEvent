package com.example.bangkitevent.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bangkitevent.data.remote.local.entity.EventEntity
import com.example.bangkitevent.data.remote.local.repository.EventRepo
import com.example.bangkitevent.data.remote.response.EventResponse
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.data.remote.retrofit.ApiConfig
import com.example.bangkitevent.ui.settings.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val eventRepo: EventRepo) : ViewModel() {
    companion object{
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEventsItem = MutableLiveData<List<ListEventsItem>?>()
    val listEventsItem: LiveData<List<ListEventsItem>?> = _listEventsItem

    private val _listEventsItemFinished = MutableLiveData<List<ListEventsItem>?>()
    val listEventsItemFinished: LiveData<List<ListEventsItem>?> = _listEventsItemFinished


    init {
        showUpcomingEvent()
        showFinishedEvents()
    }

    private fun showUpcomingEvent(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _listEventsItem.value = it.listEvents
//                        Log.d(TAG, "onResponse: Success - ${it.listEvents}")

                        // Save to local database
                        saveEventsToDatabase(it.listEvents, false)
                    } ?: run {
//                        Log.e(TAG, "onResponse: Failure - Response body is null")
                    }
                } else {
//                    Log.e(TAG, "onResponse: Failure - ${response.code()} - ${response.message()}")
//                    Toast.makeText(, "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message.toString()}")
//                Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // search API with id
    private fun showFinishedEvents(){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFinishedEvents()
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        _listEventsItemFinished.value = it.listEvents
                        Log.d(TAG, "onResponse: Success - ${it.listEvents}")

                        // Save to local database
                        saveEventsToDatabase(it.listEvents, true)
                    } ?: run {
//                        Log.e(TAG, "onResponse: Failure - Response body is null")

                    }
                } else {
//                    Log.e(TAG, "onResponse: Failure - ${response.code()} - ${response.message()}")
//                    Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
//                Log.e(TAG, "onFailure: ${t.message.toString()}")
//                Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // save local
    private fun saveEventsToDatabase(events: List<ListEventsItem>, isFinished: Boolean) {
        viewModelScope.launch {
            eventRepo.saveEventsToDatabase(events, isFinished)
        }
    }
}