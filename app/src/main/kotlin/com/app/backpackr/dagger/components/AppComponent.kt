package com.app.backpackr.dagger.components

import android.location.LocationManager
import com.app.backpackr.BackPackRApp
import com.app.backpackr.api.models.BaseUrl
import com.app.backpackr.api.repositories.RecognizedLocationsRepositoryImpl
import com.app.backpackr.dagger.modules.AppModule
import com.app.backpackr.dagger.modules.NetworkModule
import com.app.backpackr.textprocessor.services.PlacesRecognitionService
import dagger.Component
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
@Singleton
interface AppComponent {
    fun inject(application: BackPackRApp)
    fun inject(recognizedRecognizedLocationsRepositoryImpl: RecognizedLocationsRepositoryImpl)
    fun inject(placesRecognitionService: PlacesRecognitionService)
    fun locationManager(): LocationManager
    fun realm(): Realm
    fun baseUrl(): BaseUrl
    fun okHttpClient(): OkHttpClient
    fun retrofit(): Retrofit
}