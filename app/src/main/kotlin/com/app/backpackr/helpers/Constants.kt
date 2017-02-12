package com.app.backpackr.helpers

/**
 * Created by konstie on 30.12.16.
 */

class Constants {
    companion object Keys {
        val EXTRA_AUTO_FOCUS = "EXTRA_AUTO_FOCUS"
        val EXTRA_USE_FLASH = "EXTRA_USE_FLASH"
        val EXTRA_CAPTURED_SIGNS = "EXTRA_CAPTURED_SIGNS"
        val EXTRA_FETCHED_LOCATIONS = "EXTRA_FETCHED_LOCATIONS"
        val EXTRA_PROGRESS_STATUS = "EXTRA_PROGRESS_STATUS"
        val EXTRA_STORED_BACKGROUND_PICTURE = "EXTRA_STORED_BACKGROUND_PICTURE"
        val EXTRA_COORDINATES_STRING = "EXTRA_COORDINATES_STRING"
    }

    object PermissionCodes {
        val CAMERA_REQUEST_CODE = 42
        val FINE_LOCATION_REQUEST_CODE = 43
    }
}

class Actions {
    companion object Actions {
        val ACTION_PLACES_FETCHING_PROGRESS = "ACTION_PLACES_FETCHING_PROGRESS"
        val ACTION_PLACES_FETCHED = "ACTION_PLACES_FETCHED"
    }
}

class ApiParams {
    companion object {
        val HTTPS_SCHEME = "https"
        val PHOTO = "photo"
        val MAX_WIDTH = "maxWidth"
        val MAX_HEIGHT = "maxHeight"
        val KEY = "key"
        val PHOTO_REFERENCE = "photoreference"
    }
}