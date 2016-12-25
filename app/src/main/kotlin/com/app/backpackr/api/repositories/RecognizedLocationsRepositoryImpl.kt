package com.app.backpackr.api.repositories

import android.content.Context
import com.app.backpackr.BackPackRApp
import com.app.backpackr.api.models.Location
import com.app.backpackr.api.models.Place
import com.app.backpackr.dagger.components.AppComponent
import io.realm.Realm
import io.realm.RealmResults
import rx.Observable
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class RecognizedLocationsRepositoryImpl(var context: Context) : RecognizedLocationsRepository {
    var realmComponent: AppComponent
    @Inject lateinit var realmDatabase: Realm

    init {
        realmComponent = BackPackRApp.appComponent
        realmComponent.inject(this)
    }

    override fun addLocation(title: String, country: String, city: String, location: Location, address: String): Observable<Place> {
        return Observable.create<Place> {
            realmDatabase.beginTransaction()
            val newPlace = Place()
            newPlace.title = title
            newPlace.country = country
            newPlace.city = city
            newPlace.location = location
            newPlace.address = address
            realmDatabase.copyFromRealm(newPlace)
            realmDatabase.commitTransaction()
        }
    }

    override fun recognizedLocations(): Observable<RealmResults<Place>> {
        return realmDatabase.where(Place::class.java).findAllAsync().asObservable()
    }
}