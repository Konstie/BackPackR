package com.app.backpackr.ui.sections.loading

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.annotation.DrawableRes
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.api.models.Place
import com.app.backpackr.helpers.Actions
import com.app.backpackr.helpers.Constants
import com.app.backpackr.ui.sections.abs.FullScreenActivity
import java.util.*

class LoadingActivity : FullScreenActivity() {
    val TAG = LoadingActivity::class.java.simpleName

    var placesReceiver: PlacesReceiver? = null
    val handler: Handler = Handler()
    val backgroundPictures: Array<Int> = arrayOf(R.drawable.loading_bg_1, R.drawable.loading_bg_2, R.drawable.loading_bg_3, R.drawable.loading_bg_4, R.drawable.loading_bg_5)
    @DrawableRes var currentBackgroundPicture: Int? = null
    @BindView(R.id.loading_secondary_label_view) lateinit var loadingProgressTextView: TextView
    @BindView(R.id.epic_bg_image_view) lateinit var backgroundImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFullScreen()
        setContentView(R.layout.activity_loading)
        ButterKnife.bind(this)
        if (savedInstanceState != null) {
            currentBackgroundPicture = savedInstanceState.getInt(Constants.EXTRA_STORED_BACKGROUND_PICTURE)
        } else {
            setupBackgroundImage()
        }
        placesReceiver = PlacesReceiver()
    }

    private fun setupBackgroundImage() {
        currentBackgroundPicture = getRandomBackgroundPicture()
        backgroundImageView.setImageResource(currentBackgroundPicture!!)
    }

    private fun registerPlacesReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Actions.ACTION_PLACES_FETCHED)
        intentFilter.addAction(Actions.ACTION_PLACES_FETCHING_PROGRESS)
        LocalBroadcastManager.getInstance(this).registerReceiver(placesReceiver, intentFilter)
    }

    private fun onPlacesLoaded(retrievedPlaces: ArrayList<Place>) {
        Log.d(TAG, "onPlacesLoaded: " + retrievedPlaces)
    }

    override fun onStart() {
        super.onStart()
        registerPlacesReceiver()
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(placesReceiver)
    }

    private fun getRandomBackgroundPicture(): Int {
        val randomIndex = Random().nextInt(backgroundPictures.size)
        return backgroundPictures[randomIndex]
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(Constants.EXTRA_STORED_BACKGROUND_PICTURE, currentBackgroundPicture!!)
    }

    inner class PlacesReceiver : BroadcastReceiver() {
        @Suppress("UNCHECKED_CAST")
        override fun onReceive(p0: Context?, data: Intent?) {
            if (data?.action == Actions.ACTION_PLACES_FETCHING_PROGRESS) {
                loadingProgressTextView.text = data?.getStringExtra(Constants.EXTRA_PROGRESS_STATUS)
            } else if (data?.action == Actions.ACTION_PLACES_FETCHED) {
                val retrievedPlaces = data?.getSerializableExtra(Constants.EXTRA_FETCHED_LOCATIONS)
                onPlacesLoaded(retrievedPlaces as ArrayList<Place>)
            }
        }
    }
}