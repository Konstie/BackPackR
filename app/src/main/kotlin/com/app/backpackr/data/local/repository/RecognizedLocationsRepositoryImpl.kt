package com.app.backpackr.data.local.repository

import com.app.backpackr.data.local.models.Place
import com.app.backpackr.data.network.models.LocationDTO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults

class RecognizedLocationsRepositoryImpl(var realmDatabase: Realm) : RecognizedLocationsRepository {
    override fun addLocation(title: String, country: String, city: String, location: LocationDTO, address: String): Completable {
        return Completable.fromCallable {
            realmDatabase.beginTransaction()
            val newPlace = Place()
            newPlace.title = title
            newPlace.country = country
            newPlace.city = city
            newPlace.lat = location.latitude
            newPlace.long = location.longitude
            newPlace.address = address
            realmDatabase.copyFromRealm(newPlace)
            realmDatabase.commitTransaction()
        }
    }

    override fun removePlace(place: Place): Completable {
        return Completable.fromCallable {  }
    }

    override fun updatePlace(place: Place): Single<Place> {
        return Single.just(place)
    }

    override fun recognizedLocations(): Observable<RealmResults<Place>> {
        return Observable.just(realmDatabase.where(Place::class.java).findAll())
    }

    override fun savePlacesFound(placesList: RealmList<Place>): Completable {
        return Completable.fromCallable {  }
    }

    override fun findSpotWithId(spotId: Int): Single<RealmResults<Place>> {
        return Single.fromCallable {
            return@fromCallable realmDatabase.where(Place::class.java).equalTo("id", spotId).findAllAsync()
        }
    }

    override fun findSpotsInTheCity(cityName: String): Observable<RealmResults<Place>> {
        return Observable.fromCallable {
            return@fromCallable realmDatabase.where(Place::class.java).contains("address", cityName).findAllAsync()
        }
    }
}