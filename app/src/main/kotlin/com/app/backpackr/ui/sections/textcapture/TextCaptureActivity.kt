package com.app.backpackr.ui.sections.textcapture

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.textcapture.ITextCaptureView
import com.app.backpackr.presenters.textcapture.TextCapturePresenter
import com.app.backpackr.textprocessor.OcrDetectorProcessor
import com.app.backpackr.ui.sections.abs.BaseActivity
import com.app.backpackr.ui.views.GraphicOverlay
import com.app.backpackr.ui.views.OcrGraphic
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class TextCaptureActivity(override val presenterFactory: PresenterFactory<TextCapturePresenter>) : BaseActivity<TextCapturePresenter, ITextCaptureView>() {
    val TEXT_BLOCK_OBJECT = "TEXT_BLOCK_OBJECT"

    var presenter: TextCapturePresenter? = null
    var cameraSource: CameraSource? = null
    var gestureDetector: GestureDetector? = null
    var scaleGestureDetector: ScaleGestureDetector? = null
    lateinit var graphicOverlay: GraphicOverlay<OcrGraphic>

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        // todo: if permission granted
        createCameraSource()

        gestureDetector = GestureDetector(this, captureGestureListener)
        scaleGestureDetector = ScaleGestureDetector(this, scaleListener)
    }

    fun createCameraSource() {
        val context = applicationContext
        val textRecognizer = TextRecognizer.Builder(context).build()
        textRecognizer.setProcessor(OcrDetectorProcessor(graphicOverlay))

        if (!textRecognizer.isOperational) {
            Log.w(tag(), "Camera detector dependencies are not available")

            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

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

    private fun onTap(rawX: Float, rawY: Float): Boolean {
        val ocrGraphic = graphicOverlay.getGraphicAtLocation(rawX, rawY)
        val text: TextBlock? = ocrGraphic?.textBlock?: return false
        val intent = Intent()
        intent.putExtra(TEXT_BLOCK_OBJECT, text?.value)
        setResult(CommonStatusCodes.SUCCESS, intent)
        finish()
        return text != null
    }

    private val captureGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            if (e == null) return false
            return onTap(e.rawX, e.rawY) || super.onSingleTapConfirmed(e)
        }
    }

    private val scaleListener = object : ScaleGestureDetector.OnScaleGestureListener {
        override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
            return true
        }

        override fun onScaleEnd(p0: ScaleGestureDetector?) {
            // todo: do zoom
        }

        override fun onScale(p0: ScaleGestureDetector?): Boolean {
            return false
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