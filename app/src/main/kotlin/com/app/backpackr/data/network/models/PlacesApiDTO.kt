package com.app.backpackr.data.network.models

import com.squareup.moshi.Json

/**
 * Created by konstie on 01.01.17.
 */

data class BasePlaceDTO(var results: List<PlaceDTO>)

data class LocationDTO(
        @Json(name = "lat") val latitude: Double,
        @Json(name = "lng") val longitude: Double
)

data class OpeningHoursDTO(@Json(name = "open_now") val openNow: Boolean)

data class PhotoDTO(
        val height: Int, val width: Int, @Json(name = "photo_reference") val photoReference: String
)

data class PlaceDTO(
        var geometry: PlaceGeometryDTO,
        var icon: String,
        var name: String? = null,
        @Json(name = "opening_hours") var openingHours: OpeningHoursDTO,
        var photos: List<PhotoDTO>,
        @Json(name = "price_level") var priceLevel: Int,
        var rating: Double,
        var types: List<String>,
        @Json(name = "place_id") var placeId: String,
        @Json(name = "formatted_address") var formattedAddress: String
)

data class PlaceGeometryDTO(val location: LocationDTO)