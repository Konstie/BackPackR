package com.app.backpackr.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.app.backpackr.data.local.models.Place
import com.app.backpackr.textprocessor.services.PlacesRecognitionService
import com.app.backpackr.ui.sections.details.PlacesDetailsActivity
import com.app.backpackr.ui.sections.details.PlacesFoundActivity
import com.app.backpackr.ui.sections.loading.LoadingActivity
import com.app.backpackr.ui.sections.textcapture.TextCaptureActivity
import java.util.*

class IntentHelper {
    companion object IntentFactory {
        fun createOcrCameraIntent(context: Context) : Intent = Intent(context, TextCaptureActivity::class.java)

        fun createApplicationSettingsIntent(context: Context): Intent {
            val settingsIntent = Intent()
            settingsIntent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            settingsIntent.addCategory(Intent.CATEGORY_DEFAULT)
            settingsIntent.data = Uri.parse("package:" + context.packageName)
            settingsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_NO_HISTORY and Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            return settingsIntent
        }

        fun createPlacesRecognitionIntent(context: Context, capturedSigns: ArrayList<String>, coordinatesString: String): Intent {
            val placesLoaderIntent = Intent(context, PlacesRecognitionService::class.java)
            placesLoaderIntent.putStringArrayListExtra(Constants.Keys.EXTRA_CAPTURED_SIGNS, capturedSigns)
            placesLoaderIntent.putExtra(Constants.Keys.EXTRA_COORDINATES_STRING, coordinatesString)
            return placesLoaderIntent
        }

        fun createLoadingIntent(context: Context, spots: ArrayList<String>): Intent {
            val loadingIntent = Intent(context, LoadingActivity::class.java)
            loadingIntent.putStringArrayListExtra(Constants.Keys.EXTRA_CAPTURED_SIGNS, spots)
            return loadingIntent
        }

        fun createPlacesFoundIntent(context: Context): Intent {
            val placesFoundIntent = Intent(context, PlacesFoundActivity::class.java)
            return placesFoundIntent
        }

        fun createPlacesDetailsIntent(context: Context, retrievedPlaces: ArrayList<Place>): Intent {
            val detailsIntent = Intent(context, PlacesDetailsActivity::class.java)
            detailsIntent.putExtra(Constants.Keys.EXTRA_FETCHED_LOCATIONS, retrievedPlaces)
            return detailsIntent
        }
    }
}