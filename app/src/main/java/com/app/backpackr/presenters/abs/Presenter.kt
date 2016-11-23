package com.app.backpackr.presenters.abs

/**
 * Created by konstie on 13.11.16.
 */

interface Presenter<V> {
    fun onViewAttached(view: V)
    fun onViewDetached()
    fun onDestroyed()
}