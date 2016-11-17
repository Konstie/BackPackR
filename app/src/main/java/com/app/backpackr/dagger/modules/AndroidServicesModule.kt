package com.app.backpackr.dagger.modules

import android.content.Context
import android.location.LocationManager
import android.view.LayoutInflater
import com.app.backpackr.BackPackrApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by konstie on 12.11.16.
 */
@Module
class AndroidServicesModule(private val app : BackPackrApplication) {
    @Provides
    @Singleton
    fun provideAppContext() : Context {
        return app
    }

    @Provides
    @Singleton
    fun provideLocationManager() : LocationManager {
        return app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    @Singleton
    fun provideLayoutInflater() : LayoutInflater {
        return app.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}