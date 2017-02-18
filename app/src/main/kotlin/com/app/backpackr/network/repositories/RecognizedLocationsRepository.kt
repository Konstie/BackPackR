package com.app.backpackr.network.repositories

import com.app.backpackr.network.models.Place
import com.app.backpackr.network.models.dto.LocationDTO
import io.realm.RealmResults
import rx.Observable

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

interface RecognizedLocationsRepository {
    fun addLocation(title: String, country: String, city: String, location: LocationDTO, address: String): Observable<Place>
    fun removePlace(place: Place)
    fun updatePlace(place: Place)
    fun recognizedLocations(): Observable<RealmResults<Place>>
}