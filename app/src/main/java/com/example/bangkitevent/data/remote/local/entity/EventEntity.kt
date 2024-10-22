package com.example.bangkitevent.data.remote.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val id: String,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "ownerName")
    val ownerName: String? = null,

    @field:ColumnInfo(name = "summary")
    val summary: String? = null,

    @field:ColumnInfo(name = "imageLogo")
    val imageLogo: String? = null,

    @field:ColumnInfo(name = "mediaCover")
    val mediaCover: String? = null,


    @field:ColumnInfo(name = "quota")
    val quota: Int? = null,

    @field:ColumnInfo(name = "registrants")
    val registrants: Int? = null,

    @field:ColumnInfo(name = "quotaLeft")
    val quotaLeft: Int? = null,

    @field:ColumnInfo(name = "beginTime")
    val beginTime: String? = null,

    @field:ColumnInfo(name = "description")
    val description: String? = null,

    @field:ColumnInfo(name = "deepLik")
    val deepLink: String? = null,

    @field:ColumnInfo(name = "favorite")
    var isFavorite: Boolean,

    @field:ColumnInfo(name = "status")
    val isFinished: Boolean
)