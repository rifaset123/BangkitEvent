package com.example.bangkitevent

import android.os.Bundle
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bangkitevent.databinding.ActivityMainBinding
import com.example.bangkitevent.ui.finished.FinishedFragment.Companion.EXTRA_QUERY2
import com.example.bangkitevent.ui.upcoming.UpcomingFragment
import com.example.bangkitevent.ui.upcoming.UpcomingFragment.Companion.EXTRA_QUERY
import com.example.bangkitevent.ui.upcoming.UpcomingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var adapter: EventAdapter
    private lateinit var upcomingFragment: UpcomingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // search
        upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]
        upcomingFragment = UpcomingFragment()
        adapter = EventAdapter(upcomingFragment)
        upcomingViewModel.listEventsItem.observe(this) { events ->
            adapter.submitList(events ?: emptyList())
        }



        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished, R.id.navigation_favorite, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_detail, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView

        // disable search button in home fragment, settings, fav
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            search?.isVisible = destination.id != R.id.navigation_home
                    && destination.id != R.id.navigation_settings
                    && destination.id != R.id.navigation_favorite
        }
        searchView?.let {
            it.queryHint = "Search Event"
            it.isSubmitButtonEnabled = true
            it.setOnQueryTextListener(this)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            try {
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                val bundle = Bundle().apply {
                    putString(EXTRA_QUERY, it)
                }
                val bundle2 = Bundle().apply {
                    putString(EXTRA_QUERY2, it)
                }
                when (navController.currentDestination?.id) {
                    R.id.navigation_home -> navController.navigate(R.id.navigation_home, bundle)
                    R.id.navigation_upcoming -> navController.navigate(R.id.navigation_upcoming, bundle)
                    R.id.navigation_finished -> navController.navigate(R.id.navigation_finished, bundle2)
                    else -> Toast.makeText(this, "Unknown destination", Toast.LENGTH_SHORT).show()
                }
                // Hide the keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                currentFocus?.clearFocus()
            } catch (_: Exception) {
                Toast.makeText(this, "Search failed", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "No results found for $query", Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "Searching $query", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query.isNullOrEmpty()) {
            // Handler when query is empty return to original event
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            val bundle = Bundle().apply {
                putString(EXTRA_QUERY, "%default")
            }
            val bundle2 = Bundle().apply {
                putString(EXTRA_QUERY2, "%default")
            }
            when (navController.currentDestination?.id) {
                R.id.navigation_home -> navController.navigate(R.id.navigation_home, bundle)
                R.id.navigation_upcoming -> navController.navigate(R.id.navigation_upcoming, bundle)
                R.id.navigation_finished -> navController.navigate(R.id.navigation_finished, bundle2)
                else -> Toast.makeText(this,"unknown destination", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "Return to default Value", Toast.LENGTH_SHORT).show()
        }
        return true
    }

}