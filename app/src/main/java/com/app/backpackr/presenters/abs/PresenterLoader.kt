package com.app.backpackr.presenters.abs

import android.content.Context
import android.content.Loader

/**
 * Created by konstie on 13.11.16.
 */

class PresenterLoader<V, P : Presenter<V>>(val factory: PresenterFactory<V, P>, var presenter : P? = null, context: Context?) : Loader<P>(context) {
    override fun onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter)
            return
        }
        forceLoad()
    }

    override fun onForceLoad() {
        presenter = factory.create()
        deliverResult(presenter)
    }

    override fun onReset() {
        presenter?.onDestroyed()
        presenter = null
    }
}