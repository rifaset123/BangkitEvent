package com.example.bangkitevent.ui.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bangkitevent.R
import com.example.bangkitevent.data.remote.local.entity.EventEntity
import com.example.bangkitevent.databinding.ItemCardViewBinding

class FavoriteAdapter(private val onFavoriteClick: (EventEntity) -> Unit) : ListAdapter<EventEntity, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK)  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            onFavoriteClick(event)
        }
    }

    class MyViewHolder(val binding: ItemCardViewBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(event: EventEntity) {
            binding.apply {
                // Bind data to views
                binding.itemTitle.text = event.name
                Glide.with(binding.root.context)
                    .load(event.imageLogo) // URL Gambar
                    .centerCrop()
                    .placeholder(R.drawable.ng)
                    .into(binding.itemImage) // imageView
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<EventEntity> =
            object : DiffUtil.ItemCallback<EventEntity>() {
                override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem.name == newItem.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

