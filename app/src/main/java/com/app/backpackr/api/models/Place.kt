package com.app.backpackr.api.models

import io.realm.RealmObject

/**
 * Created by konstie on 17.11.16.
 */

class Place : RealmObject() {
    var title : String? = null
    var city : String? = null
    var country : String? = null
    var address : String? = null
    var location : Location? = null
}