package com.app.backpackr

import android.app.Application
import com.app.backpackr.injection.components.AppComponent
import com.app.backpackr.injection.components.DaggerAppComponent
import com.app.backpackr.injection.components.DaggerDatabaseComponent
import com.app.backpackr.injection.components.DatabaseComponent
import com.app.backpackr.injection.modules.AppModule
import io.realm.Realm

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */

class BackPackRApp : Application() {
    companion object {
        lateinit var appComponent: AppComponent
        lateinit var databaseComponent: DatabaseComponent
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
        databaseComponent = DaggerDatabaseComponent.builder()
                .build()
    }
}