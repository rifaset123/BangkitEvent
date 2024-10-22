package com.example.bangkitevent.data.remote.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    // MediatorLiveData digunakan untuk menggabungkan dua sumber data (LiveData) yang berbeda
    private val result = MediatorLiveData<Result<List<EventEntity>>>()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // LiveData Builder
//    fun getEvents(): LiveData<Result<List<EventEntity>>> = liveData{
//        emit(Result.Loading) // bukan merupakan LiveData, gunakan fungsi emit
//        val client = apiService.getEvents() // Mengambil dari network dengan ApiService
//
//        try {
//            val response = client.execute()
//            if (response.isSuccessful) {
//                val articles = response.body()?.listEvents ?: emptyList()
//
//                val newsList = articles.map { article ->
//                    val isBookmarked = newsDao.isNewsBookmarked(article.id ?: "")
//                    EventEntity(
//                        article.id ?: "",
//                        article.name ?: "",
//                        article.description ?: "",
//                        article.mediaCover ?: "",
//                        article.quota ?: 0,
//                        article.beginTime ?: "",
//                        article.description ?: "",
//                        article.link ?: "",
//                        isBookmarked
//
//                    )
//                }
//                Log.d("EventRepo", "getHeadlineNews: $newsList")
//                newsDao.deleteAll()
//                newsDao.insertEvent(newsList)
//            } else {
//                emit(Result.Error("Failed to fetch data"))
//            }
//        } catch (e: Exception) {
//            Log.d("EventRepo", "getHeadlineNews: ${e.message.toString()} ")
//            emit(Result.Error(e.message.toString()))
//        }
//        val localData: LiveData<Result<List<EventEntity>>> = newsDao.getNews().map { Result.Success(it) }
//        emitSource(localData) // LiveData, gunakan fungsi emitSource.
//    }


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

    // hanya mengubah sebuah value, yakni isBookmarked dan meng-update-nya pada database
    suspend fun setFavorite(events: EventEntity, isFavorite: Boolean) {
        // hapus penggunaan appExecutor karena pakek coroutine
        events.isFavorite = isFavorite
        eventDao.updateNews(events)
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
            newsDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepo =
            instance ?: synchronized(this) {
                instance ?: EventRepo(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}