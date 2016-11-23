package com.app.backpackr.presenters.abs

/**
 * Created by konstie on 13.11.16.
 */

interface PresenterFactory<V, out T : Presenter<V>> {
    fun create(): T
}