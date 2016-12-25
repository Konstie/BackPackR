package com.app.backpackr

import android.app.Application
import com.app.backpackr.dagger.components.AppComponent
import com.app.backpackr.dagger.components.DaggerAppComponent
import com.app.backpackr.dagger.components.DaggerNetworkComponent
import com.app.backpackr.dagger.components.NetworkComponent
import com.app.backpackr.dagger.modules.AppModule

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class BackPackRApp : Application() {
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var networkComponent: NetworkComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        networkComponent = DaggerNetworkComponent.builder().build()
    }


}