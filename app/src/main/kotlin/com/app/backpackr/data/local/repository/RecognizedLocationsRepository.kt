package com.app.backpackr.data.local.repository

import com.app.backpackr.data.local.models.Place
import com.app.backpackr.data.network.models.LocationDTO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.RealmList
import io.realm.RealmResults

interface RecognizedLocationsRepository {
    fun addLocation(title: String, country: String, city: String, location: LocationDTO, address: String): Completable
    fun removePlace(place: Place): Completable
    fun updatePlace(place: Place): Single<Place>
    fun recognizedLocations(): Observable<RealmResults<Place>>
    fun savePlacesFound(placesList: RealmList<Place>): Completable
    fun findSpotWithId(spotId: Int): Single<RealmResults<Place>>
    fun findSpotsInTheCity(cityName: String): Observable<RealmResults<Place>>
}