package com.example.bangkitevent.utils

import com.example.bangkitevent.data.remote.response.ListEventsItem

interface OnEventClickListener {
    // onclick handler
    fun onEventClick(event: ListEventsItem)
}