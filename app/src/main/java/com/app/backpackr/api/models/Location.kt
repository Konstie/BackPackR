package com.app.backpackr.api.models

import io.realm.RealmObject

/**
 * Created by konstie on 17.11.16.
 */
class Location : RealmObject() {
    var long : Double = 0.0
    var lat : Double = 0.0
}