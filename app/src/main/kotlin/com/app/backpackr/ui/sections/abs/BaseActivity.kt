package com.app.backpackr.ui.sections.abs

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.util.SparseIntArray
import com.app.backpackr.R
import com.app.backpackr.helpers.IntentHelper

import com.app.backpackr.presenters.abs.Presenter
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.abs.PresenterLoader

abstract class BaseActivity<P : Presenter<V>, V> : FullScreenActivity(), LoaderManager.LoaderCallbacks<P> {
    private var presenter: Presenter<V>? = null
    private var permissionsString: SparseIntArray = SparseIntArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportLoaderManager.initLoader(loaderId(), null, this)
    }

    override fun onStart() {
        super.onStart()
        activitiesTracker.onActivityWentToForeground()
    }

    override fun onPause() {
        activitiesTracker.onActivityWentToBackground()
        super.onPause()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionGrantedStatus = PackageManager.PERMISSION_GRANTED
        grantResults.forEach { permission -> permissionGrantedStatus += permission }
        if ((grantResults.isNotEmpty()) && permissionGrantedStatus == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode)
        } else {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.permission_warning, permissionsString.get(requestCode)), Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.action_accept, {
                        startActivity(IntentHelper.createApplicationSettingsIntent(this@BaseActivity))
                    }).show()
        }
    }

    fun requestAppPermissions(requestedPermissions: Array<String>, stringId: Int, requestCode: Int) {
        permissionsString.put(requestCode, stringId)
        var permissionStatus = PackageManager.PERMISSION_GRANTED
        var shouldShowRationale = false
        requestedPermissions.forEach {
            permissionStatus += ContextCompat.checkSelfPermission(this, it)
            shouldShowRationale = shouldShowRationale || ActivityCompat.shouldShowRequestPermissionRationale(this, it)
        }
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.action_grant, {
                            ActivityCompat.requestPermissions(this@BaseActivity, requestedPermissions, requestCode)
                        }).show()
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, requestCode)
            }
        } else {
            onPermissionsGranted(requestCode)
        }
    }

    protected abstract fun onPermissionsGranted(requestCode: Int)

    protected abstract fun tag(): String

    protected abstract val presenterFactory: PresenterFactory<P>

    protected abstract fun onPresenterPrepared(presenter: P)

    protected fun onPresenterDestroyed() {
    }

    protected val presenterView: V
        get() = this as V

    protected fun loaderId(): Int {
        return LOADER_ID
    }

    override fun onDestroy() {
        presenter?.onViewDetached()
        super.onDestroy()
    }

    companion object {
        private val LOADER_ID = 101
    }
}
