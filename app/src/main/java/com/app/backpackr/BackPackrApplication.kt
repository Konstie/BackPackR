package com.app.backpackr

import android.app.Application
import com.app.backpackr.dagger.components.NetworkComponent

/**
 * Created by konstie on 12.11.16.
 */
class BackPackrApplication(private var networkComponent: NetworkComponent) : Application() {

    override fun onCreate() {
        super.onCreate()
//        networkComponent = DaggerNetworkComponent.builder()
//            .androidServicesModule(AndroidServicesModule(this))
//            .networkModule(NetworkModule(ApiBaseUrls.GOOGLE_PLACES.toString()))
//            .build()
    }

    fun getNetworkComponent() : NetworkComponent {
        return networkComponent;
    }
}