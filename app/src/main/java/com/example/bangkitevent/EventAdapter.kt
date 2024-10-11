package com.example.bangkitevent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitevent.data.response.EventResponse
import com.example.bangkitevent.data.response.ListEventsItem
import com.example.bangkitevent.util.OnEventClickListener

class EventAdapter(private var events: List<ListEventsItem>, private val listener: OnEventClickListener) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEvent: ImageView = itemView.findViewById(R.id.item_image)
        val eventName: TextView = itemView.findViewById(R.id.item_title)
        var itemId: Int = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_view, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        // preview data
        holder.eventName.text = event.name
        Glide.with(holder.itemView.context)
            .load(event.imageLogo) // URL Gambar
            .centerCrop()
            .placeholder(R.drawable.ng)
            .into(holder.imgEvent) // imageView mana yang akan diterapkan

        // click handler
        holder.itemView.setOnClickListener {
            listener.onEventClick(event)
        }
    }

    override fun getItemCount() = events.size

    fun updateEvents(newEvents: List<ListEventsItem>) {
        events = newEvents
        notifyDataSetChanged()
    }

}