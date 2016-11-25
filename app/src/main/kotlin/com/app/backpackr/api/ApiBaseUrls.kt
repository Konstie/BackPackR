package com.app.backpackr.api

/**
 * Created by konstie on 12.11.16.
 */
enum class ApiBaseUrls(private val baseUrl: String) {
    GOOGLE_PLACES("https://maps.googleapis.com/maps/api/place/");

    override open fun toString() : String {
        return baseUrl
    }
}
