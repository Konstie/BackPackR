package com.app.backpackr.dagger.components

import com.app.backpackr.BackPackrApplication
import com.app.backpackr.dagger.modules.AndroidServicesModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by konstie on 12.11.16.
 */
@Singleton
@Component(modules = arrayOf(AndroidServicesModule::class))
interface BackPackrAppComponent {
    fun inject(application : BackPackrApplication)
}