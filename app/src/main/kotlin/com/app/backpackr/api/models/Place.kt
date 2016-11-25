package com.app.backpackr.api.models

import io.realm.RealmObject

/**
 * Created by konstie on 17.11.16.
 */

open class Place : RealmObject() {
    open var title : String? = null
    open var city : String? = null
    open var country : String? = null
    open var address : String? = null
    open var location : Location? = null

    val description: String
        get() = "$country, $city, $address"
}