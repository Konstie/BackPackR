package com.app.backpackr.presenters.abs

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

interface PresenterFactory<T : Presenter<*>> {
    fun create(): T
}
