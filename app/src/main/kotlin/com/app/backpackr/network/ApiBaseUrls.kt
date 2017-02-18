package com.app.backpackr.network

/**
 * Created by konstie on 25.12.16.
 */
enum class ApiBaseUrls(private val baseUrl: String) {
    GOOGLE_PLACES("https://maps.googleapis.com/maps/api/place/");

    override open fun toString() : String {
        return baseUrl
    }
}