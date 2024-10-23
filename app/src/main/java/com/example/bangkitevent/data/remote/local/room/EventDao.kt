package com.example.bangkitevent.data.remote.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bangkitevent.data.remote.local.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY beginTime DESC")
    fun getEvents(): LiveData<List<EventEntity>>

    @Query("UPDATE events SET favorite = :isFavorite WHERE id = :eventId")
    suspend fun updateEventFavoriteStatus(eventId: String, isFavorite: Boolean)

    @Query("SELECT * FROM events where favorite = 1")
    fun getFavoriteEvents(): LiveData<List<EventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEvent(events: List<EventEntity>)

    // suspend function untuk mendukung Coroutines
    @Update
    suspend fun updateNews(news: EventEntity)

    @Query("SELECT EXISTS(SELECT * FROM events WHERE id = :eventId AND favorite = 1)")
    suspend fun isFavorited(eventId: String): Boolean
}