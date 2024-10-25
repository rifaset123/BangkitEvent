package com.example.bangkitevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bangkitevent.EventAdapter
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.databinding.FragmentHomeBinding
import com.example.bangkitevent.ui.ViewModelFactory
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.bangkitevent.ui.settings.SettingsViewModel
import com.example.bangkitevent.utils.OnEventClickListener

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
        val homeViewModel: HomeViewModel by viewModels {
            ViewModelFactory.getInstance(requireContext())
        }
        val settingsModel: SettingsViewModel by viewModels {
            ViewModelFactory.getInstance(requireContext())
        }


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

        homeViewModel.isLoading2.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar32.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // observe listEventsItem
        homeViewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            val limitedEvents = events?.take(5) ?: emptyList() // max 5 item
            adapter.submitList(limitedEvents)
//            Log.d("HomeFragment", "RecyclerView loaded with ${limitedEvents.size} items")
        }

        // observe finished listEventsItem
        homeViewModel.listEventsItemFinished.observe(viewLifecycleOwner) { events ->
            val limitedEvents = events?.take(5) ?: emptyList() // max 5 item
            adapterUpcoming.submitList(limitedEvents)
//            Log.d("HomeFragment", "RecyclerView loaded with ${limitedEvents.size} items")
        }

        settingsModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEventClick(event: ListEventsItem) {
        // Handle the click event
//        Log.d("FinishedFragmentClickTest", "Event clicked: ${event.id}")
        event.id?.let { id ->
//            Log.d("FinishedFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
            val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
            startActivity(intentToDetail)
        } ?: run {
            Toast.makeText(requireContext(), "Event is null", Toast.LENGTH_SHORT).show()
        }
    }
}