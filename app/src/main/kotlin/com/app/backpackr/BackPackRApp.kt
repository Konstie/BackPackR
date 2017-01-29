package com.app.backpackr

import android.app.Application
import com.app.backpackr.dagger.components.AppComponent
import com.app.backpackr.dagger.components.DaggerAppComponent
import com.app.backpackr.dagger.modules.AppModule
import io.realm.Realm

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class BackPackRApp : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}