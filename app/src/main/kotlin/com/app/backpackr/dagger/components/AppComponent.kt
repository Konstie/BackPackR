package com.app.backpackr.dagger.components

import android.location.LocationManager
import com.app.backpackr.BackPackRApp
import com.app.backpackr.api.repositories.RecognizedLocationsRepositoryImpl
import com.app.backpackr.dagger.modules.AppModule
import com.app.backpackr.textprocessor.services.PlacesRecognitionService
import dagger.Component
import io.realm.Realm
import javax.inject.Singleton

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */
@Component(modules = arrayOf(AppModule::class))
@Singleton
interface AppComponent {
    fun inject(application: BackPackRApp)
    fun inject(recognizedRecognizedLocationsRepositoryImpl: RecognizedLocationsRepositoryImpl)
    fun inject(placesRecognitionService: PlacesRecognitionService)
    fun locationManager(): LocationManager
    fun realm(): Realm
}