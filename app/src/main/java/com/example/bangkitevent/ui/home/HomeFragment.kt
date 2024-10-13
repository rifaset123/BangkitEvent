package com.example.bangkitevent.ui.home

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bangkitevent.EventAdapter
import com.example.bangkitevent.data.response.ListEventsItem
import com.example.bangkitevent.databinding.FragmentHomeBinding
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.bangkitevent.util.OnEventClickListener

class HomeFragment : Fragment(), OnEventClickListener {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // finished
        val recyclerViewUpcoming: RecyclerView = binding.recyclerViewUpcomingEvents
        val adapterUpcoming = EventAdapter(this)
        recyclerViewUpcoming.adapter = adapterUpcoming
        recyclerViewUpcoming.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // upcoming
        val recyclerView: RecyclerView = binding.recyclerViewFinishedEvents
        val adapter = EventAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        // observe loading
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // observe listEventsItem
        homeViewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }

        // observe finished listEventsItem
        homeViewModel.listEventsItemFinished.observe(viewLifecycleOwner) { events ->
            adapterUpcoming.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClick(event: ListEventsItem) {
        // Handle the click event
        Log.d("FinishedFragmentClickTest", "Event clicked: ${event.id}")
        event.id?.let { id ->
            Log.d("FinishedFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
            val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
            startActivity(intentToDetail)
        } ?: run {
            Log.e("FinishedFragmentClickTest", "Event ID is null")
        }
    }
}