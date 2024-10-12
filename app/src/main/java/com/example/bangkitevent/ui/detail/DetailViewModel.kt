package com.example.bangkitevent.ui.detail

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bangkitevent.data.response.Event
import com.example.bangkitevent.data.response.EventIDResponse
import com.example.bangkitevent.data.response.EventResponse
import com.example.bangkitevent.data.response.ListEventsItem
import com.example.bangkitevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _detailEvent = MutableLiveData<Event?>()
    val detailEvent: MutableLiveData<Event?> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listEventsItem = MutableLiveData<List<ListEventsItem>?>()
    val listEventsItem: LiveData<List<ListEventsItem>?> = _listEventsItem

    // LiveData to handle errors
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun fetchEventDetail(eventID: String) {
        _isLoading.value = true
        Log.d("DetailViewModel", "Fetching event details for ID: $eventID")

        // Call the API service to get event details
        val client = ApiConfig.getApiService().getDetailEvent(eventID)
        client.enqueue(object : Callback<EventIDResponse> {
            override fun onResponse(call: Call<EventIDResponse>, response: Response<EventIDResponse>) {
                _isLoading.value = false
                Log.d("DetailViewModel", "API response received")

                // Check if the response is successful and body is not null
                if (response.isSuccessful) {
                    response.body()?.let { eventID ->
                        // store the value to LiveData _detailEvent
                        _detailEvent.value = eventID.event
                        Log.d("DetailViewModel", "onResponse: Success - ${eventID}")

                    } ?: run {
                        Log.e("DetailViewModel", "onResponse: Failure - Response body is null")
                    }
                } else {
                    // Handle error response
                    _error.value = "Error ${response.code()}: ${response.message()}"
                    Log.e("DetailViewModel", "onResponse: Failure - ${response.code()} - ${response.message()}")
                    Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventIDResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message ?: "Unknown error"
                Log.e("DetailViewModel", "onFailure: ${t.message}")
                Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}