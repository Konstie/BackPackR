package com.app.backpackr.ui.sections.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.backpackr.R

@Suppress("EXPERIMENTAL_FEATURE_WARNING")
class PlacesFoundActivity : AppCompatActivity() {
    val TAG = PlacesFoundActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places_found)
    }
}