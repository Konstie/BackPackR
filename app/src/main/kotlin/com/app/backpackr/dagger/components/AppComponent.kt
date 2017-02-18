package com.app.backpackr.dagger.components

import com.app.backpackr.BackPackRApp
import com.app.backpackr.network.repositories.RecognizedLocationsRepositoryImpl
import com.app.backpackr.dagger.modules.AppModule
import com.app.backpackr.dagger.modules.NetworkModule
import com.app.backpackr.presenters.loading.LoadingPresenter
import com.app.backpackr.textprocessor.services.PlacesRecognitionService
import com.app.backpackr.ui.sections.abs.BaseActivity
import com.app.backpackr.ui.sections.abs.FullScreenActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
@Singleton
interface AppComponent {
    fun inject(application: BackPackRApp)
    fun inject(baseActivity: FullScreenActivity)
    fun inject(recognizedRecognizedLocationsRepositoryImpl: RecognizedLocationsRepositoryImpl)
    fun inject(placesRecognitionService: PlacesRecognitionService)
    fun inject(loadingPresenter: LoadingPresenter)
}