package com.app.backpackr.ui.sections.abs

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.util.Log

import com.app.backpackr.presenters.abs.Presenter
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.abs.PresenterLoader

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

abstract class BaseActivity<P : Presenter<V>, V> : AppCompatActivity(), LoaderManager.LoaderCallbacks<P> {
    private var presenter: Presenter<V>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportLoaderManager.initLoader(loaderId(), null, this)
    }

    override fun onLoadFinished(loader: Loader<P>?, presenter: P) {
        this@BaseActivity.presenter = presenter
        onPresenterPrepared(presenter)
    }

    override fun onLoaderReset(loader: Loader<P>?) {
        this@BaseActivity.presenter = null
        onPresenterDestroyed()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<P> {
        return PresenterLoader(this@BaseActivity, presenterFactory, tag())
    }

    override fun onStart() {
        super.onStart()
        presenter?.onViewAttached(presenterView)
    }

    override fun onStop() {
        presenter?.onViewDetached()
        super.onStop()
    }

    protected abstract fun tag(): String

    /**
     * Instance of [PresenterFactory] use to create a Presenter when needed. This instance should
     * not contain [android.app.Activity] context reference since it will be keep on rotations.
     */
    protected abstract val presenterFactory: PresenterFactory<P>

    /**
     * Hook for subclasses that deliver the [Presenter] before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract fun onPresenterPrepared(presenter: P)

    /**
     * Hook for subclasses before the screen gets destroyed.
     */
    protected fun onPresenterDestroyed() {
    }

    /**
     * Override in case of fragment not implementing Presenter<View> interface
    </View> */
    protected val presenterView: V
        get() = this as V

    /**
     * Use this method in case you want to specify a spefic ID for the [PresenterLoader].
     * By default its value would be [.LOADER_ID].
     */
    protected fun loaderId(): Int {
        return LOADER_ID
    }

    companion object {
        private val TAG = BaseActivity::class.java.name
        private val LOADER_ID = 101
    }
}
