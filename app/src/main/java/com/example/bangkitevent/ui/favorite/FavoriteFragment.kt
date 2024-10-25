package com.example.bangkitevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bangkitevent.databinding.FragmentFavoriteBinding
import com.example.bangkitevent.ui.ViewModelFactory
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID


class FavoriteFragment : Fragment(){
    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root

        val favoriteViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[FavoriteViewModel::class.java]

        val recyclerView: RecyclerView = binding.rVFavorite
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val adapter = FavoriteAdapter(onFavoriteClick = { eventEntity ->
            // Handle the favorite click event
            Log.d("FinishedFragmentClickTest", "Event clicked: ${eventEntity.id}")
            eventEntity.id.let { id ->
                Log.d("FinishedFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
                val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(EXTRA_ID, id)
                }
                startActivity(intentToDetail)
            }
        })
        recyclerView.adapter = adapter

        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide loading indicator
            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        favoriteViewModel.favoriteEventsEntity.observe(viewLifecycleOwner){ events ->
            // Update the UI with the list of favorite events
            if (events != null) {
                adapter.submitList(events)
                Log.d("FavoriteFragment", "RecyclerView loaded with ${events.size} items")
                if (events.isEmpty()){
                    binding.noEventText.visibility = View.VISIBLE
                    binding.eventHeader.visibility = View.GONE
                } else {
                    binding.noEventText.visibility = View.GONE
                    binding.eventHeader.visibility = View.VISIBLE
                }
            }
        }

        favoriteViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            // Show or hide loading indicator
            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
