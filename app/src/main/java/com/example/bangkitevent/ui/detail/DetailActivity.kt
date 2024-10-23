package com.example.bangkitevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bangkitevent.R
import com.example.bangkitevent.databinding.ActivityDetailBinding
import com.example.bangkitevent.ui.ViewModelFactory
import com.example.bangkitevent.ui.home.HomeViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import kotlin.math.abs
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object{
        const val EXTRA_ID = "0"
        const val TAB_FAVORITE = "favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleTextView: TextView = findViewById(R.id.detail_title)
        val detailOwner: TextView = findViewById(R.id.detail_owner)
        val summaryTextView: TextView = findViewById(R.id.detail_summary)
        val kuotaTextView: TextView = findViewById(R.id.detail_kuota)
        val beginTimeTextView: TextView = findViewById(R.id.detail_beginTime)
        val descriptionTextView: TextView = findViewById(R.id.detail_description)
        val detailButton: MaterialButton = findViewById(R.id.detail_button)

        // bookmark,
        val favorite : FloatingActionButton = findViewById(R.id.fab_add)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this)).get(DetailViewModel::class.java)

        // get event ID from intent
//        Log.d("DetailActivityTest", "Getting event ID from intent ${intent?.getStringExtra(EXTRA_ID)}")
        val eventId = intent?.getStringExtra(EXTRA_ID)

        // fetch event details
        eventId?.let {
            viewModel.fetchEventDetail(it)
        }

        // observe loading
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        //observe data
        viewModel.detailEvent.observe(this) { event ->
            event?.let {
                // update UI
                setSupportActionBar(findViewById(R.id.toolbar))
                val appBarLayout: AppBarLayout = findViewById(R.id.app_bar)
                appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                    if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                        // collapse
                        binding.toolbarLayout.title = it.name
                    } else {
                        // expanded
                        binding.toolbarLayout.title = " "
                    }
                }
                Glide.with(this@DetailActivity)
                    .load(it.mediaCover)
                    .into(binding.toolbarLayout.findViewById(R.id.image_placeholder))
                titleTextView.text = it.name
                detailOwner.text = it.ownerName
                summaryTextView.text = it.summary
                // sisa kuota (quota - registrants)
                kuotaTextView.text = (it.registrants?.let { it1 -> it.quota?.minus(it1) } ?: "N/A").toString()
                beginTimeTextView.text = it.beginTime
                descriptionTextView.text = HtmlCompat.fromHtml(
                    it.description.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                val deepLink = it.link
                // deeplink (hyperlink buat ke web)
                detailButton.setOnClickListener {
                    val intentDeeplink = Intent(Intent.ACTION_VIEW, Uri.parse("$deepLink"))
                    startActivity(intentDeeplink)
                }

                // observe favorite status
                viewModel.isEventFavorited(it.id.toString()).observe(this) { isFavorite ->
                    viewModel.setFavoriteStatus(it.id.toString(), isFavorite)
                }

                // favorite button ketika di klik akan menambahkan ke favorite
                favorite.setOnClickListener {
                    event?.let { eventEntity ->
                        viewModel.isEventFavorited(eventEntity.id.toString()).observe(this) { isFavorite ->
                            viewModel.setFavoriteStatus(eventEntity.id.toString(), !isFavorite)
                        }
                    }
                }

                // Observe favorite status ganti ikon
                viewModel.favoriteStatus.observe(this) { isFavorite ->
//                    Log.d("DetailActivity", "Favorite status for event $eventId: $isFavorite")
                    if (isFavorite) {
                        favorite.setImageDrawable(ContextCompat.getDrawable(favorite.context, R.drawable.ic_favorite_24))
                    } else {
                        favorite.setImageDrawable(ContextCompat.getDrawable(favorite.context, R.drawable.ic_favorite_border_24))
                    }
                }
            } ?: run {
//                Log.d("DetailActivityTESTT", "Event not found")
            }
        }
    }
}