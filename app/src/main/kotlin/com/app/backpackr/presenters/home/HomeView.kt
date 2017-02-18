package com.app.backpackr.presenters.home

import com.app.backpackr.network.models.Place
import io.realm.RealmResults

/**
 * Created by konstie on 17.11.16.
 */

interface HomeView {
    fun onLocationsLoaded(savedLocations: RealmResults<Place>)
    fun onLocationsLoadingStopped()
}