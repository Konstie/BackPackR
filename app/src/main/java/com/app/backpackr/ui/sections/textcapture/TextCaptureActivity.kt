package com.app.backpackr.ui.sections.textcapture

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.textcapture.ITextCaptureView
import com.app.backpackr.presenters.textcapture.TextCapturePresenter
import com.app.backpackr.textprocessor.OcrDetectorProcessor
import com.app.backpackr.ui.sections.abs.BaseActivity
import com.app.backpackr.ui.views.GraphicOverlay
import com.app.backpackr.ui.views.OcrGraphic
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextRecognizer

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class TextCaptureActivity(override val presenterFactory: PresenterFactory<TextCapturePresenter>) : BaseActivity<TextCapturePresenter, ITextCaptureView>() {
    var presenter: TextCapturePresenter? = null
    var cameraSource: CameraSource? = null
    lateinit var graphicOverlay: GraphicOverlay<OcrGraphic>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val context = applicationContext
        val textRecognizer = TextRecognizer.Builder(context).build()
        textRecognizer.setProcessor(OcrDetectorProcessor(graphicOverlay))

        if (!textRecognizer.isOperational) {
            Log.w(tag(), "Camera detector dependencies are not available")

            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            var hasLowStorage = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                Log.w(tag(), "Yout device has low storage")
            }
        } else {
            cameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(15.0f)
                    .build()
        }
    }

    override fun onPresenterPrepared(presenter: TextCapturePresenter) {
        this@TextCaptureActivity.presenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        // todo: release the camera detector
    }

    override fun tag(): String {
        return TextCaptureActivity::class.java.name
    }
}