package com.example.bangkitevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bangkitevent.R
import com.example.bangkitevent.databinding.ActivityDetailBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import kotlin.math.abs

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    companion object{
        const val EXTRA_ID = "0"
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

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        // get event ID from intent
        Log.d("DetailActivityTest", "Getting event ID from intent ${intent?.getStringExtra(EXTRA_ID)}")
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
                    .load(it.imageLogo)
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
            } ?: run {
                Log.d("DetailActivityTESTT", "Event not found")
            }
        }
    }
}