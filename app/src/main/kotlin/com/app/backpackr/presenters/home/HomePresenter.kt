package com.app.backpackr.presenters.home

import android.content.Context
import android.util.Log
import com.app.backpackr.BackPackRApp
import com.app.backpackr.data.local.repository.RecognizedLocationsRepositoryImpl
import com.app.backpackr.presenters.abs.Presenter
import io.realm.Realm
import javax.inject.Inject

/**
 * Created by konstie on 17.11.16.
 */

class HomePresenter(var context : Context) : Presenter<HomeView> {
    val TAG = HomePresenter::class.java.name

    var view: HomeView? = null
    val locationsRepository: RecognizedLocationsRepositoryImpl

    @Inject lateinit var realm: Realm

    init {
        BackPackRApp.databaseComponent.inject(this)
        locationsRepository = RecognizedLocationsRepositoryImpl(realm)
    }

    fun fetchSavedLocations() {
        locationsRepository.recognizedLocations()
                .cache()
                .subscribe({
                    cachedPlaces -> view?.onLocationsLoaded(cachedPlaces)
                }, { throwable -> run {
                        Log.e(TAG, "Could not load places list: " + throwable.message)
                        view?.onLocationsLoadingStopped()
                    }
                }, { view?.onLocationsLoadingStopped() })
    }

    override fun onViewAttached(view: HomeView) {
        this.view = view
    }

    override fun onViewDetached() {
        this.view = null
    }

    override fun onDestroyed() {

    }
}