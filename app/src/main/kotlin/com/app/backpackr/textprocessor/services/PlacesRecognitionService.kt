package com.app.backpackr.textprocessor.services

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import com.app.backpackr.BackPackRApp
import com.app.backpackr.R
import com.app.backpackr.api.models.Place
import com.app.backpackr.api.models.dto.PlaceDTO
import com.app.backpackr.api.services.PlacesRetrievalService
import com.app.backpackr.dagger.components.AppComponent
import com.app.backpackr.helpers.Constants
import retrofit2.Retrofit
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by konstie on 30.12.16.
 */

class PlacesRecognitionService : IntentService(PlacesRecognitionService::class.java.name) {
    val TAG = PlacesRecognitionService::class.java.name
    val LOCATION_REFRESH_TIME = 5000L
    val LOCATION_REFRESH_DISTANCE = 10f
    val PLACES_NEARBY_RADIUS = 5000
    var detectedSignsList: List<String>? = null

    @Inject lateinit var locationManager: LocationManager
    @Inject lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()
        val appComponent: AppComponent = BackPackRApp.appComponent;
        appComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onHandleIntent(data: Intent?) {
        detectedSignsList = data?.getStringArrayListExtra(Constants.EXTRA_CAPTURED_SIGNS)
        sendProgressBroadcast()
        retrieveCurrentLocation()
    }

    private fun retrieveCurrentLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                val lat = location?.latitude
                val long = location?.longitude
                val coordsString = getCoordsString(lat?: 0.0, long?: 0.0)
                Log.d(TAG, "Location retrieved: $coordsString")
                sendProgressBroadcast()
                fetchPlacesInfo(coordsString)
            }

            override fun onProviderDisabled(p0: String?) {}

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

            override fun onProviderEnabled(p0: String?) {}
        })
    }

    private fun getCoordsString(lat: Double, long: Double): String {
        return "$lat,$long"
    }

    private fun fetchPlacesInfo(coordsString: String) {
        val placesService: PlacesRetrievalService = retrofit.create(PlacesRetrievalService::class.java)
        placesService.getAllPlacesByCriteria(
                coordsString, PLACES_NEARBY_RADIUS, detectedSignsList?: ArrayList<String>(), getString(R.string.google_places_api_key))
                .flatMap { placesResults -> Observable.from(placesResults.results) }
                .filter { place -> place.name != null }
                .toList()
                .subscribe {
                    places ->
                }
    }

    private fun createPlaceItems(rawPlaces: List<PlaceDTO>) {
        val placeItems = ArrayList<Place>()
        for (place in rawPlaces) {
            var newPlace = Place()
            newPlace.title = place.name
            newPlace.address = place.formattedAddress
            newPlace.lat = place.geometry.location.latitude
            newPlace.long = place.geometry.location.longitude
            newPlace.placeDetailsReference = place.placeId
        }
    }

    private fun sendProgressBroadcast() {

    }
}