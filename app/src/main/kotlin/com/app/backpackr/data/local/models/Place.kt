package com.app.backpackr.data.local.models

import android.net.Uri
import com.app.backpackr.data.network.ApiBaseUrls
import com.app.backpackr.utils.Constants
import io.realm.RealmObject
import java.io.Serializable

/**
 * Created by konstie on 17.11.16.
 */

open class Place : RealmObject(), Serializable {
    open var title : String? = null
    open var city : String? = null
    open var country : String? = null
    open var address : String? = null
    open var photoReference: String? = null
    open var placeDetailsReference: String? = null
    open var long : Double = 0.0
    open var lat : Double = 0.0

    val description: String
        get() = "$country, $city, $address"

    fun getImageUrl(maxWidth: Int, apiKey: String): String {
        val uriBuilder = Uri.Builder()
        uriBuilder.scheme(Constants.ApiParams.HTTPS_SCHEME)
                .authority(ApiBaseUrls.GOOGLE_PLACES.name)
                .appendPath(Constants.ApiParams.PHOTO)
                .appendQueryParameter(Constants.ApiParams.MAX_WIDTH, maxWidth.toString())
                .appendQueryParameter(Constants.ApiParams.PHOTO_REFERENCE, photoReference)
                .appendQueryParameter(Constants.ApiParams.KEY, apiKey)
        return uriBuilder.toString()
    }

    override fun toString(): String {
        return "Place (title = $title, city = $city, country = $country, address = $address, " +
                "photoReference = $photoReference, placeDetailsReference = $placeDetailsReference, " +
                "long = $long, lat = $lat)"
    }


}