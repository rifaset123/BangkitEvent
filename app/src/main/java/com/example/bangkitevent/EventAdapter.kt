package com.example.bangkitevent

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.databinding.ItemCardViewBinding
import com.example.bangkitevent.utils.OnEventClickListener

class EventAdapter(private val listener: OnEventClickListener) : ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    companion object {
        // check data different
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                val result = oldItem == newItem
                Log.d("EventAdapter", "areItemsTheSame: $result")
                return result
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                val result = oldItem == newItem
                Log.d("EventAdapter", "areContentsTheSame: $result")
                return result
            }
        }
    }

    class EventViewHolder(private val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.itemTitle.text = event.name
            Glide.with(binding.root.context)
                .load(event.imageLogo) // URL Gambar
                .centerCrop()
                .placeholder(R.drawable.ng)
                .into(binding.itemImage) // imageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            listener.onEventClick(event)
        }
    }

    override fun getItemCount() = currentList.size

}