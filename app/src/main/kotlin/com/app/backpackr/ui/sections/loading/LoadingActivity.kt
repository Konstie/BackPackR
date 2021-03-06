package com.app.backpackr.ui.sections.loading

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.data.local.models.Place
import com.app.backpackr.utils.Constants
import com.app.backpackr.utils.IntentHelper
import com.app.backpackr.utils.RuntimePermissionsHelper
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.loading.ILoadingView
import com.app.backpackr.presenters.loading.LoadingPresenter
import com.app.backpackr.ui.sections.abs.BaseActivity
import java.util.*

class LoadingActivity : BaseActivity<LoadingPresenter, ILoadingView>(), ILoadingView {
    private val TAG: String = LoadingActivity::class.java.simpleName

    private var placesReceiver: PlacesReceiver? = null
    private var loadingPresenter: LoadingPresenter? = null

    val backgroundPictures: Array<Int> = arrayOf(R.drawable.loading_bg_1, R.drawable.loading_bg_2, R.drawable.loading_bg_3, R.drawable.loading_bg_4, R.drawable.loading_bg_5)

    var currentBackgroundPicture: Int? = null
    var capturedSigns: ArrayList<String>? = null

    @BindView(R.id.loading_secondary_label_view) lateinit var loadingProgressTextView: TextView
    @BindView(R.id.epic_bg_image_view) lateinit var backgroundImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFullScreen()
        setContentView(R.layout.activity_loading)
        ButterKnife.bind(this)
        currentBackgroundPicture = savedInstanceState?.getInt(Constants.Keys.EXTRA_STORED_BACKGROUND_PICTURE)
        capturedSigns = intent.getStringArrayListExtra(Constants.Keys.EXTRA_CAPTURED_SIGNS)
        setupBackgroundImage()
        placesReceiver = PlacesReceiver()
    }

    private fun setupBackgroundImage() {
        if (currentBackgroundPicture == null) {
            currentBackgroundPicture = getRandomBackgroundPicture()
        }
        backgroundImageView.setImageResource(currentBackgroundPicture!!)
    }

    override fun onCurrentLocationFetched(coordinatesString: String) {
        startLoadingPlacesInfo(capturedSigns, coordinatesString)
    }

    private fun startLoadingPlacesInfo(capturedSigns: ArrayList<String>?, coordinatesString: String) {
        startService(IntentHelper.createPlacesRecognitionIntent(this, capturedSigns ?: ArrayList<String>(), coordinatesString))
    }

    private fun onPlacesLoaded(retrievedPlaces: ArrayList<Place>) {
        startActivity(IntentHelper.createPlacesFoundIntent(this@LoadingActivity))
        finish()
    }

    override fun onStart() {
        super.onStart()
        registerPlacesReceiver()
    }

    private fun registerPlacesReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.Actions.ACTION_PLACES_FETCHED)
        intentFilter.addAction(Constants.Actions.ACTION_PLACES_FETCHING_PROGRESS)
        registerReceiver(placesReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(placesReceiver)
    }

    private fun getRandomBackgroundPicture(): Int {
        val randomIndex = Random().nextInt(backgroundPictures.size)
        return backgroundPictures[randomIndex]
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(Constants.Keys.EXTRA_STORED_BACKGROUND_PICTURE, currentBackgroundPicture!!)
    }

    inner class PlacesReceiver : BroadcastReceiver() {
        @Suppress("UNCHECKED_CAST")
        override fun onReceive(p0: Context?, data: Intent?) {
            Log.d(TAG, "onReceive from broadcast receiver")
            if (data?.action == Constants.Actions.ACTION_PLACES_FETCHING_PROGRESS) {
                loadingProgressTextView.text = data.getStringExtra(Constants.Keys.EXTRA_PROGRESS_STATUS)
            } else if (data?.action == Constants.Actions.ACTION_PLACES_FETCHED) {
                val retrievedPlaces = data.getSerializableExtra(Constants.Keys.EXTRA_FETCHED_LOCATIONS)
                onPlacesLoaded(retrievedPlaces as ArrayList<Place>)
            }
        }
    }

    override fun onPresenterPrepared(presenter: LoadingPresenter) {
        if (loadingPresenter != null) {
            return
        }
        this.loadingPresenter = presenter
        this.loadingPresenter?.onViewAttached(this@LoadingActivity)
        if (RuntimePermissionsHelper.checkFineLocationPermission(this@LoadingActivity)) {
            this.loadingPresenter?.defineCurrentLocation()
        } else {
            requestAppPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), R.string.permission_warning, Constants.PermissionCodes.FINE_LOCATION_REQUEST_CODE)
        }
    }

    override fun onPermissionsGranted(requestCode: Int) {
        if (requestCode == Constants.PermissionCodes.FINE_LOCATION_REQUEST_CODE) {
            loadingPresenter?.defineCurrentLocation()
        }
    }

    override val presenterFactory: PresenterFactory<LoadingPresenter>
        get() = object : PresenterFactory<LoadingPresenter> {
            override fun create(): LoadingPresenter {
                return LoadingPresenter()
            }
        }
}