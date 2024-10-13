package com.example.bangkitevent.util

import com.example.bangkitevent.data.response.ListEventsItem

interface OnEventClickListener {
    // onclick handler
    fun onEventClick(event: ListEventsItem)
}