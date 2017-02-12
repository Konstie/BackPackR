package com.app.backpackr.textprocessor.services

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.app.backpackr.BackPackRApp
import com.app.backpackr.R
import com.app.backpackr.api.models.Place
import com.app.backpackr.api.models.dto.PlaceDTO
import com.app.backpackr.api.services.PlacesRetrievalService
import com.app.backpackr.helpers.Actions
import com.app.backpackr.helpers.ActivitiesTracker
import com.app.backpackr.helpers.Constants
import com.app.backpackr.ui.sections.details.PlacesDetailsActivity
import retrofit2.Retrofit
import rx.Observable
import java.util.*
import javax.inject.Inject

class PlacesRecognitionService : IntentService(PlacesRecognitionService::class.java.name) {
    private val TAG = PlacesRecognitionService::class.java.name

    private val PLACES_NEARBY_RADIUS = 5000

    lateinit @Inject var retrofit: Retrofit
    lateinit @Inject var activitiesTracker: ActivitiesTracker

    override fun onCreate() {
        super.onCreate()
        BackPackRApp.appComponent.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onHandleIntent(data: Intent?) {
        Log.d(TAG, "Starting places recognition service")
        val detectedSignsList = data?.getStringArrayListExtra(Constants.EXTRA_CAPTURED_SIGNS) ?: emptyList<String>()
        val currentCoordinates = data?.getStringExtra(Constants.EXTRA_COORDINATES_STRING) ?: "0.0, 0.0"
        fetchPlacesInfo(detectedSignsList, currentCoordinates)
    }

    private fun fetchPlacesInfo(capturedSigns: List<String>, coordsString: String) {
        val placesService: PlacesRetrievalService = retrofit.create(PlacesRetrievalService::class.java)
        placesService.getAllPlacesByCriteria(
                coordsString, PLACES_NEARBY_RADIUS, capturedSigns, getString(R.string.google_places_api_key))
                .flatMap { placesResults -> Observable.from(placesResults.results) }
                .filter { place -> place.name != null }
                .toList()
                .doOnNext { Log.d(TAG, "Retrieved place: " + it) }
                .subscribe({ places -> createPlaceItems(places) },
                        { throwable -> Log.e(TAG, "Could not retrieve places: " + throwable.message) }
                )
    }

    private fun createPlaceItems(rawPlaces: List<PlaceDTO>) {
        Log.d(TAG, "Creating place items: " + rawPlaces.size)
        val placeItems = ArrayList<Place>()
        for (place in rawPlaces) {
            val newPlace = Place()
            newPlace.title = place.name
            newPlace.address = place.formattedAddress
            newPlace.lat = place.geometry.location.latitude
            newPlace.long = place.geometry.location.longitude
            newPlace.placeDetailsReference = place.placeId
            placeItems.add(newPlace)
        }
        if (activitiesTracker.isApplicationInBackground()) {
            sendPlacesInfoRetrievalNotification(placeItems)
        } else {
            sendPlacesBroadcast(placeItems)
        }
        stopSelf()
    }

    private fun sendPlacesInfoRetrievalNotification(places: ArrayList<Place>) {
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification_new_place)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getNotificationDescription(places.size))
        val placeDetailsIntent = Intent(this, PlacesDetailsActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(PlacesDetailsActivity::class.java)
        stackBuilder.addNextIntent(placeDetailsIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(places[0].hashCode(), notificationBuilder.build())
    }

    private fun getNotificationDescription(placesCount: Int): String {
        if (placesCount == 0) {
            return getString(R.string.notification_title_could_not_find_places)
        } else {
            val placesString = if (placesCount == 1) getString(R.string.place) else getString(R.string.places)
            return getString(R.string.notification_title_retrieved_place_info, placesCount, placesString)
        }
    }

    private fun sendPlacesBroadcast(foundPlaces: ArrayList<Place>) {
        val broadcastIntent = Intent(Actions.ACTION_PLACES_FETCHED)
        broadcastIntent.putExtra(Constants.Keys.EXTRA_FETCHED_LOCATIONS, foundPlaces)
        sendBroadcast(broadcastIntent)
    }
}