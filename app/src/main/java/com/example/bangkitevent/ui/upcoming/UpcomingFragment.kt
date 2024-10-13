package com.example.bangkitevent.ui.upcoming

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
import com.example.bangkitevent.EventAdapter
import com.example.bangkitevent.data.response.ListEventsItem
import com.example.bangkitevent.databinding.FragmentUpcomingBinding
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.bangkitevent.util.OnEventClickListener

class UpcomingFragment : Fragment() , OnEventClickListener {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object{
        const val EXTRA_QUERY = ""
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val recyclerView: RecyclerView = binding.rvUpcoming
        val adapter = EventAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // observe loading
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar1.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // observe listEventsItem
        if (arguments?.containsKey(EXTRA_QUERY) == true) {
            val query = arguments?.getString(EXTRA_QUERY)
            Log.d("UpcomingFragmentTest", "Search query: $query")
            if (query == "%default") {
                observeStoredData(upcomingViewModel, adapter)
                Log.d("UpcomingFragment", "RecyclerView loaded with stored data")
            } else {
                if (query != null) {
                    upcomingViewModel.searchEvents(query)
                }
                observeListEventsItem(upcomingViewModel, adapter)
            }
        }else{
            observeListEventsItem(upcomingViewModel, adapter)
            Log.d("UpcomingFragment", "RecyclerView loaded ")
        }

        return root

    }

    private fun observeListEventsItem(upcomingViewModel: UpcomingViewModel, adapter: EventAdapter) {
        // observe listEventsItem
        upcomingViewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }
    }
    private fun observeStoredData(upcomingViewModel: UpcomingViewModel, adapter: EventAdapter) {
        // observe listEventsItem
        upcomingViewModel.storedDefault.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}