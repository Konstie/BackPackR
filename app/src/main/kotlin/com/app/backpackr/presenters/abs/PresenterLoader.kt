package com.app.backpackr.presenters.abs

import android.content.Context
import android.support.v4.content.Loader
import android.util.Log

class PresenterLoader<T : Presenter<*>>(context: Context, private val factory: PresenterFactory<T>) : Loader<T>(context) {
    private var presenter: T? = null

    override fun onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter as T)
            return
        }

        forceLoad()
    }

    override fun onForceLoad() {
        presenter = factory.create()

        deliverResult(presenter as T)
    }

    override fun deliverResult(data: T) {
        super.deliverResult(data)
    }

    override fun onStopLoading() {
    }

    override fun onReset() {
        if (presenter != null) {
            presenter?.onDestroyed()
            presenter = null
        }
    }
}