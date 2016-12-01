package com.app.backpackr.dagger.components

import android.location.LocationManager
import com.app.backpackr.BackPackRApp
import com.app.backpackr.dagger.modules.AppModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: BackPackRApp)
    fun locationManager(): LocationManager
}