package com.example.bangkitevent.ui.finished

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
import com.example.bangkitevent.databinding.FragmentFinishedBinding
import com.example.bangkitevent.ui.detail.DetailActivity
import com.example.bangkitevent.ui.detail.DetailActivity.Companion.EXTRA_ID
import com.example.bangkitevent.util.OnEventClickListener


class FinishedFragment : Fragment(), OnEventClickListener {

    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var finishedViewModel: FinishedViewModel

    override fun onEventClick(event: ListEventsItem) {
        // Handle the click event
        Log.d("FinishedFragmentClickTest", "Event clicked: ${event.id}")
        event.id?.let { id ->
            Log.d("FinishedFragmentClickTest", "Navigating to DetailActivity with Event ID: $id")
            val intentToDetail = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id.toString())
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
        val finishedViewModel = ViewModelProvider(this).get(FinishedViewModel::class.java)

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView: RecyclerView = binding.rvFinished
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        val adapter = EventAdapter(emptyList<ListEventsItem>(),this)
        recyclerView.adapter = adapter

        // observe loading
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // observe listEventsItem
        finishedViewModel.listEventsItem.observe(viewLifecycleOwner) { events ->
            adapter.updateEvents(events ?: emptyList())
            Log.d("FinishedFragment", "RecyclerView loaded with ${events?.size ?: 0} items")
        }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setDetailData(eventID : ListEventsItem){
        // set data from eventID

    }
}