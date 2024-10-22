package com.example.bangkitevent.ui.detail

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bangkitevent.data.remote.local.entity.EventEntity
import com.example.bangkitevent.data.remote.local.repository.EventRepo
import com.example.bangkitevent.data.remote.local.room.EventDao
import com.example.bangkitevent.data.remote.response.Event
import com.example.bangkitevent.data.remote.response.EventIDResponse
import com.example.bangkitevent.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val eventRepo: EventRepo) : ViewModel() {

    private val _detailEvent = MutableLiveData<Event?>()
    val detailEvent: MutableLiveData<Event?> = _detailEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // favorite
    val favoriteStatus: LiveData<Boolean> = MutableLiveData()

    // LiveData to handle errors
    private val _error = MutableLiveData<String>()


    fun fetchEventDetail(eventID: String) {
        _isLoading.value = true
        Log.d("DetailViewModel", "Fetching event details for ID: $eventID")

        // call API
        val client = ApiConfig.getApiService().getDetailEvent(eventID)
        client.enqueue(object : Callback<EventIDResponse> {
            override fun onResponse(
                call: Call<EventIDResponse>,
                response: Response<EventIDResponse>
            ) {
                _isLoading.value = false
                Log.d("DetailViewModel", "API response received")

                // check if response is successful
                if (response.isSuccessful) {
                    response.body()?.let { eventID ->
                        // store the value to LiveData _detailEvent
                        _detailEvent.value = eventID.event
                        Log.d("DetailViewModel", "onResponse: Success - $eventID")
                    } ?: run {
                        Log.e("DetailViewModel", "onResponse: Failure - Response body is null")
                    }
                } else {
                    // Handle error response
                    _error.value = "Error ${response.code()}: ${response.message()}"
                    Log.e(
                        "DetailViewModel",
                        "onResponse: Failure - ${response.code()} - ${response.message()}"
                    )
//                    Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventIDResponse>, t: Throwable) {
                _isLoading.value = false
                _error.value = t.message ?: "Unknown error"
                Log.e("DetailViewModel", "onFailure: ${t.message}")
//                Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // fitur bookmark

    fun getBookmarkedNews() = eventRepo.getFavoriteEvents()

    // apply coroutine
    fun saveNews(events: EventEntity) {
        viewModelScope.launch {
            eventRepo.setFavorite(events, true)
        }
    }

    fun deleteNews(events: EventEntity) {
        viewModelScope.launch {
            eventRepo.setFavorite(events, false)
        }
    }

    // favorite
    fun setFavoriteStatus(eventId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            eventRepo.updateEventFavoriteStatus(eventId, isFavorite)
            (favoriteStatus as MutableLiveData).value = isFavorite
        }
    }

    fun isEventFavorited(eventId: String): LiveData<Boolean> {
        return eventRepo.isEventFavorited(eventId).also {
            it.observeForever { isFavorite ->
                (favoriteStatus as MutableLiveData).value = isFavorite
            }
        }
    }
}