package com.app.backpackr.repository

import com.app.backpackr.network.models.Place
import io.realm.RealmList
import io.realm.RealmResults
import rx.Observable

interface PlacesRepository {
    fun savePlacesFound(placesList: RealmList<Place>)
    fun saveSpot(place: Place)
    fun removeSpot(place: Place): Boolean
    fun findSpotWithId(spotId: Int): Observable<RealmResults<Place>>
    fun findSpotsInTheCity(cityName: String): Observable<RealmResults<Place>>
}