package com.example.bangkitevent.ui.finished

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bangkitevent.data.response.EventResponse
import com.example.bangkitevent.data.response.ListEventsItem
import com.example.bangkitevent.data.retrofit.ApiConfig
import com.example.bangkitevent.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel(application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    companion object{
        private const val TAG = "MainViewModel"

    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventsName = MutableLiveData<List<EventResponse>>()
    val eventsName: LiveData<List<EventResponse>> = _eventsName

    private val _listEventsItem = MutableLiveData<List<ListEventsItem>?>()
    val listEventsItem: LiveData<List<ListEventsItem>?> = _listEventsItem

    init {
        showEvents()
    }

    private fun showEvents(){
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
                        _listEventsItem.value = it.listEvents
                        Log.d(TAG, "onResponse: Success - ${it.listEvents}")
                    } ?: run {
                        Log.e(TAG, "onResponse: Failure - Response body is null")
                    }
                } else {
                    Log.e(TAG, "onResponse: Failure - ${response.code()} - ${response.message()}")
                    Toast.makeText(getApplication(), "Failed to Fetch API", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                Toast.makeText(getApplication(), "Failed to load events: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}