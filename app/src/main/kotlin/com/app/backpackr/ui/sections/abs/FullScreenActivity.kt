package com.app.backpackr.ui.sections.abs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.app.backpackr.BackPackRApp
import com.app.backpackr.helpers.ActivitiesTracker
import javax.inject.Inject

open class FullScreenActivity : AppCompatActivity() {
    @Inject protected lateinit var activitiesTracker: ActivitiesTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BackPackRApp.appComponent.inject(this)
    }

    protected fun setupFullScreen() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}