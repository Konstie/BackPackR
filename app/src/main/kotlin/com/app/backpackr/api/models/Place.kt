package com.app.backpackr.api.models

import android.net.Uri
import com.app.backpackr.api.ApiBaseUrls
import com.app.backpackr.helpers.ApiParams
import com.app.backpackr.helpers.Constants
import io.realm.RealmObject

/**
 * Created by konstie on 17.11.16.
 */

open class Place : RealmObject() {
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
        uriBuilder.scheme(ApiParams.HTTPS_SCHEME)
                .authority(ApiBaseUrls.GOOGLE_PLACES.name)
                .appendPath(ApiParams.PHOTO)
                .appendQueryParameter(ApiParams.MAX_WIDTH, maxWidth.toString())
                .appendQueryParameter(ApiParams.PHOTO_REFERENCE, photoReference)
                .appendQueryParameter(ApiParams.KEY, apiKey)
        return uriBuilder.toString()
    }
}