package com.app.backpackr.api.repositories

import android.content.Context
import com.app.backpackr.api.models.Location
import com.app.backpackr.api.models.Place
import com.app.backpackr.dagger.components.AppComponent
import com.app.backpackr.dagger.components.DaggerAppComponent
import com.app.backpackr.helpers.rxrealm.RealmObservable
import io.realm.Realm
import io.realm.RealmResults
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class RecognizedLocationsRepositoryImpl(var context: Context) : RecognizedLocationsRepository {
    @Inject var realmComponent: AppComponent
    @Inject var realmDatabase: Realm

    init {
        realmComponent = DaggerAppComponent.builder().build()
        realmComponent.inject(this)
        realmDatabase = realmComponent.realm()
    }

    override fun addLocation(title: String, country: String, city: String, location: Location, address: String): Observable<Place> {
        realmDatabase.beginTransaction()
        val newPlace = Place()
        newPlace.title = title
        newPlace.country = country
        newPlace.city = city
        newPlace.location = location
        newPlace.address = address
        realmDatabase.commitTransaction()
        return RealmObservable.getObject(context, Func1<Realm, Place> {
            realmDatabase.copyFromRealm(newPlace)
        })
    }

    override fun recognizedLocations(): Observable<RealmResults<Place>> {
        return realmDatabase.where(Place::class.java).findAllAsync().asObservable()
    }
}