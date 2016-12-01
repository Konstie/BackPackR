package com.app.backpackr.helpers

import android.content.Context
import android.content.Intent
import com.app.backpackr.LocationDetailActivity
import com.app.backpackr.ui.sections.textcapture.TextCaptureActivity

/**
 * Created by kmikhailovskiy on 28.11.2016.
 */

class IntentHelper {
    companion object IntentFactory {
        fun createOcrCameraIntent(context: Context) : Intent = Intent(context, TextCaptureActivity::class.java)
        fun createLocationDetailsIntent(context: Context) : Intent = Intent(context, LocationDetailActivity::class.java)
    }
}