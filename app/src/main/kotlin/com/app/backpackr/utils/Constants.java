package com.app.backpackr.utils;

public class Constants {
    public interface Keys {
        String EXTRA_AUTO_FOCUS = "EXTRA_AUTO_FOCUS";
        String EXTRA_USE_FLASH = "EXTRA_USE_FLASH";
        String EXTRA_CAPTURED_SIGNS = "EXTRA_CAPTURED_SIGNS";
        String EXTRA_FETCHED_LOCATIONS = "EXTRA_FETCHED_LOCATIONS";
        String EXTRA_PROGRESS_STATUS = "EXTRA_PROGRESS_STATUS";
        String EXTRA_STORED_BACKGROUND_PICTURE = "EXTRA_STORED_BACKGROUND_PICTURE";
        String EXTRA_COORDINATES_STRING = "EXTRA_COORDINATES_STRING";
    }
    
    public interface PermissionCodes {
        int CAMERA_REQUEST_CODE = 42;
        int FINE_LOCATION_REQUEST_CODE = 43;
    }
    
    public interface Actions {
        String ACTION_PLACES_FETCHING_PROGRESS = "ACTION_PLACES_FETCHING_PROGRESS";
        String ACTION_PLACES_FETCHED = "ACTION_PLACES_FETCHED";
    }
    
    public interface ApiParams {
        String HTTPS_SCHEME = "https";
        String PHOTO = "photo";
        String MAX_WIDTH = "maxWidth";
        String MAX_HEIGHT = "maxHeight";
        String KEY = "key";
        String PHOTO_REFERENCE = "photoreference";
    }
}
