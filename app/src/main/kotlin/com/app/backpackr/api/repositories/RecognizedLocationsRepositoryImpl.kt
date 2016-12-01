package com.app.backpackr.api.repositories

import com.app.backpackr.api.models.Location
import com.app.backpackr.api.models.Place
import com.app.backpackr.dagger.components.AppComponent
import com.app.backpackr.dagger.components.DaggerAppComponent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.realm.Realm
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class RecognizedLocationsRepositoryImpl : RecognizedLocationsRepository {
    @Inject var realmComponent: AppComponent
    @Inject var realmDatabase: Realm

    init {
        realmComponent = DaggerAppComponent.builder().build()
        realmComponent.inject(this)
        realmDatabase = realmComponent.realm()
    }

    override fun addLocation(title: String, country: String, city: String, location: Location, address: String): Observable<Place> {
        return Observable.create {
            // todo: provide implementation
        }
    }

    override fun recognizedLocations(): Observable<List<Place>> {
        return Observable.create {
            // todo: provide implementation
        }
    }
}