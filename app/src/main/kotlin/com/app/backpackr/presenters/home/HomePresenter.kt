package com.app.backpackr.presenters.home

import android.content.Context
import android.util.Log
import com.app.backpackr.network.repositories.RecognizedLocationsRepositoryImpl
import com.app.backpackr.presenters.abs.Presenter

/**
 * Created by konstie on 17.11.16.
 */

class HomePresenter(var context : Context) : Presenter<HomeView> {
    val TAG = HomePresenter::class.java.name

    var view: HomeView? = null
    var locationsRepository: RecognizedLocationsRepositoryImpl

    init {
        locationsRepository = RecognizedLocationsRepositoryImpl(context)
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