package com.example.bangkitevent.util

import com.example.bangkitevent.data.response.ListEventsItem

interface OnEventClickListener {
    fun onEventClick(event: ListEventsItem)
}