package com.example.bangkitevent.ui.finished

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bangkitevent.EventAdapter
import com.example.bangkitevent.data.remote.response.ListEventsItem
import com.example.bangkitevent.databinding.FragmentFinishedBinding
import com.example.bangkitevent.ui.ViewModelFactory
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.bangkitevent.utils.OnEventClickListener


class FinishedFragment : Fragment(), OnEventClickListener {

    private var _binding: FragmentFinishedBinding? = null

    private val binding get() = _binding!!


    companion object{
        const val EXTRA_QUERY2 = "0"
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

        val finishedViewModel: FinishedViewModel by viewModels {
            ViewModelFactory.getInstance(requireContext())
        }

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val recyclerView: RecyclerView = binding.rvFinished
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        val adapter = EventAdapter(this)
        recyclerView.adapter = adapter

        // observe loading
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


        // observe listEventsItem
        if (arguments?.containsKey(EXTRA_QUERY2) == true) {
            val query = arguments?.getString(EXTRA_QUERY2)
//            Log.d("FinishedFragment", "Search query: $query")
            if (query == "%default") {
                observeStoredData(finishedViewModel, adapter)
//                Log.d("UpcomingFragment", "RecyclerView loaded with stored data")
            } else {
                if (query != null) {
                    finishedViewModel.searchEvents(query)
                }
                observeListEventsItem(finishedViewModel, adapter)
            }
        }else{
            observeListEventsItem(finishedViewModel, adapter)
//            Log.d("FinishedFragmentTest", "RecyclerView loaded ")
        }

        return root

    }
    private fun observeListEventsItem(finishedViewModel: FinishedViewModel, adapter: EventAdapter) {
        // observe listEventsItem
        finishedViewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }
    }

    // observe data if user search with no query
    private fun observeStoredData(finishedViewModel: FinishedViewModel, adapter: EventAdapter) {
        // observe listEventsItem
        finishedViewModel.storedDefault.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events ?: emptyList())
            Log.d("UpcomingFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}