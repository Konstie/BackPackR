package com.app.backpackr.presenters.abs

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

import android.content.Context
import android.support.v4.content.Loader
import android.util.Log

class PresenterLoader<T : Presenter<*>>(context: Context, private val factory: PresenterFactory<T>, private val tag: String) : Loader<T>(context) {
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
        Log.i("loader", "deliverResult-" + tag)
    }

    override fun onStopLoading() {
        Log.i("loader", "onStopLoading-" + tag)
    }

    override fun onReset() {
        Log.i("loader", "onReset-" + tag)
        if (presenter != null) {
            presenter?.onDestroyed()
            presenter = null
        }
    }
}