package com.example.bangkitevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bangkitevent.EventAdapter
import com.example.bangkitevent.R
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.databinding.FragmentFavoriteBinding
import com.example.bangkitevent.databinding.FragmentFinishedBinding
import com.example.bangkitevent.ui.ViewModelFactory
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.bangkitevent.ui.finished.FinishedFragment.Companion.EXTRA_QUERY2
import com.example.bangkitevent.ui.finished.FinishedViewModel
import com.example.bangkitevent.ui.upcoming.UpcomingViewModel
import com.example.bangkitevent.utils.OnEventClickListener

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteFragment : Fragment(){
    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root = binding.root

        val favoriteViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[FavoriteViewModel::class.java]

        val recyclerView: RecyclerView = binding.rVFavorite
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        val adapter = FavoriteAdapter(onFavoriteClick = { eventEntity ->
            // Handle the favorite click event
            Log.d("FinishedFragmentClickTest", "Event clicked: ${eventEntity.id}")
            eventEntity.id?.let { id ->
                Log.d("FinishedFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
                val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
                    putExtra(EXTRA_ID, id)
                }
                startActivity(intentToDetail)
            }
        })
        recyclerView.adapter = adapter

        favoriteViewModel.favoriteEventsEntity.observe(viewLifecycleOwner, Observer { events ->
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
        })

        favoriteViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            // Show or hide loading indicator
            binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
