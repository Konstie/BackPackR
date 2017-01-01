package com.app.backpackr.api.repositories

import com.app.backpackr.api.models.Place
import io.realm.RealmResults
import rx.Observable

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

interface RecognizedLocationsRepository {
    fun addLocation(title: String, country: String, city: String, location: Location, address: String): Observable<Place>
    fun recognizedLocations(): Observable<RealmResults<Place>>
}