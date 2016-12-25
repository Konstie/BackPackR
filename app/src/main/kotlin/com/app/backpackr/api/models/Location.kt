package com.app.backpackr.api.models

import io.realm.RealmObject

/**
 * Created by konstie on 25.12.16.
 */

open class Location : RealmObject() {
    open var long : Double = 0.0
    open var lat : Double = 0.0
}