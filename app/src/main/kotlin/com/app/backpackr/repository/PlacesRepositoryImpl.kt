package com.app.backpackr.repository

import com.app.backpackr.network.models.Place
import io.realm.RealmList
import io.realm.RealmResults
import rx.Observable

class PlacesRepositoryImpl : PlacesRepository {
    override fun savePlacesFound(placesList: RealmList<Place>) {
    }

    override fun saveSpot(place: Place) {
    }

    override fun removeSpot(place: Place): Boolean {
        return true
    }

    override fun findSpotWithId(spotId: Int): Observable<RealmResults<Place>> {
        return Observable.empty()
    }

    override fun findSpotsInTheCity(cityName: String): Observable<RealmResults<Place>> {
        return Observable.empty()
    }
}