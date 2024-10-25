package com.example.bangkitevent.data.remote.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.bangkitevent.data.remote.local.entity.EventEntity
import com.example.bangkitevent.data.remote.local.room.EventDao
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.data.remote.retrofit.ApiService
import com.example.bangkitevent.utils.AppExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class EventRepo  private constructor(
    @Suppress("unused")
    private val apiService: ApiService,
    private val eventDao: EventDao,
    @Suppress("unused")
    private val appExecutors: AppExecutors
) {
    // MediatorLiveData digunakan untuk menggabungkan dua sumber data (LiveData) yang berbeda
//    private val result = MediatorLiveData<Result<List<EventEntity>>>()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    // bookmark feature
    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    // insert event
    suspend fun insertEvents(events: List<EventEntity>) {
        eventDao.insertEvent(events)
    }

    fun saveEventsToDatabase(events: List<ListEventsItem>, isFinished: Boolean) {
        val eventEntities = events.map { event ->
            EventEntity(
                event.id ?: "",
                event.name ?: "",
                event.ownerName ?: "",
                event.summary ?: "",
                event.imageLogo ?: "",
                event.mediaCover ?: "",
                event.quota ?: 0,
                event.registrants ?: 0,
                event.quota?.minus(event.registrants ?: 0) ?: 0,
                event.beginTime ?: "",
                event.description ?: "",
                event.link ?: "",
                false,
                isFinished
            )
        }
        coroutineScope.launch {
            insertEvents(eventEntities)
        }
    }


    suspend fun updateEventFavoriteStatus(eventId: String, isFavorite: Boolean) {
        eventDao.updateEventFavoriteStatus(eventId, isFavorite)
    }

    fun isEventFavorited(eventId: String) = liveData {
        val isFavorited = eventDao.isFavorited(eventId)
        emit(isFavorited)
    }

    companion object {
        @Volatile
        private var instance: EventRepo? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
            newsDao: EventDao,
        ): EventRepo =
            instance ?: synchronized(this) {
                instance ?: EventRepo(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}