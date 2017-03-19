package com.app.backpackr.injection.modules

import android.content.Context
import android.location.LocationManager

import com.app.backpackr.BackPackRApp
import com.app.backpackr.utils.ActivitiesTracker

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

@Module
class AppModule(private val application: BackPackRApp) {
    @Provides
    @Singleton
    fun provideApplication(): BackPackRApp {
        return application
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideLocationManager(): LocationManager {
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager
    }

    @Provides
    @Singleton
    fun provideActivityTracker(): ActivitiesTracker {
        return ActivitiesTracker()
    }
}
