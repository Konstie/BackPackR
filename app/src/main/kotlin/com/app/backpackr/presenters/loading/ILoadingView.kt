package com.app.backpackr.presenters.loading

interface ILoadingView {
    fun onCurrentLocationFetched(coordinatesString: String)
}