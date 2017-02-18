package com.app.backpackr.presenters.loading

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.app.backpackr.BackPackRApp
import com.app.backpackr.presenters.abs.Presenter
import javax.inject.Inject

class LoadingPresenter : Presenter<ILoadingView> {
    private val TAG = LoadingPresenter::class.java.simpleName

    private val LOCATION_REFRESH_TIME = 5000L
    private val LOCATION_REFRESH_DISTANCE = 10f

    private var loadingView: ILoadingView? = null
    private var locationsRequested = false

    @Inject lateinit var locationManager: LocationManager

    init {
        BackPackRApp.appComponent.inject(this)
    }

    override fun onViewAttached(view: ILoadingView) {
        loadingView = view
    }

    fun defineCurrentLocation() {
        if (locationsRequested) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                val lat = location?.latitude
                val long = location?.longitude
                val coordsString = getCoordsString(lat?: 0.0, long?: 0.0)
                Log.d(TAG, "Location retrieved: $coordsString")
                loadingView?.onCurrentLocationFetched(coordsString)
                locationsRequested
            }

            override fun onProviderDisabled(p0: String?) {}

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

            override fun onProviderEnabled(p0: String?) {}
        })
    }

    private fun getCoordsString(lat: Double, long: Double): String {
        return "$lat,$long"
    }

    override fun onViewDetached() {
        loadingView = null
    }

    override fun onDestroyed() {
    }
}