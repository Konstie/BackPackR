package com.app.backpackr.dagger.modules

import android.content.Context
import android.location.LocationManager

import com.app.backpackr.BackPackRApp

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
    val REALM_DATABASE_NAME = "backpackr.realm"
    val SCHEMA_VERSION: Long = 1

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
    fun provideRealm(): Realm {
        return Realm.getInstance(getCustomRealmConfiguration())
    }

    private fun getCustomRealmConfiguration(): RealmConfiguration {
        val customRealmConfiguration = RealmConfiguration.Builder()
                .name(REALM_DATABASE_NAME)
                .schemaVersion(SCHEMA_VERSION)
                .build()
        return customRealmConfiguration
    }
}
