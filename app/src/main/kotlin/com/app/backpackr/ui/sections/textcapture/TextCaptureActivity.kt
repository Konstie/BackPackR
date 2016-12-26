package com.app.backpackr.ui.sections.textcapture

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import butterknife.BindView
import com.app.backpackr.R
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.textcapture.ITextCaptureView
import com.app.backpackr.presenters.textcapture.TextCapturePresenter
import com.app.backpackr.textprocessor.OcrDetectorProcessor
import com.app.backpackr.ui.sections.abs.BaseActivity
import com.app.backpackr.ui.views.CameraSourcePreview
import com.app.backpackr.ui.views.GraphicOverlay
import com.app.backpackr.ui.views.OcrGraphic
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class TextCaptureActivity() : BaseActivity<TextCapturePresenter, ITextCaptureView>(), ITextCaptureView {
    val RC_CANERA_PERMISSION = 2
    val TEXT_BLOCK_OBJECT = "TEXT_BLOCK_OBJECT"

    var presenter: TextCapturePresenter? = null
    var cameraSource: CameraSource? = null
    var gestureDetector: GestureDetector? = null
    var scaleGestureDetector: ScaleGestureDetector? = null
    @BindView(R.id.graphic_overlay) lateinit var graphicOverlay: GraphicOverlay<OcrGraphic>
    @BindView(R.id.camera_preview) lateinit var cameraPreview: CameraSourcePreview

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_capture_text)

        // todo: if permission granted
        val cameraPermissionStatus = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {

        }

        gestureDetector = GestureDetector(this, captureGestureListener)
        scaleGestureDetector = ScaleGestureDetector(this, scaleListener)
    }

    private fun requestCameraPermission() {
        Log.w(tag(), "requestCameraPermission called...")
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_CANERA_PERMISSION)
            return
        }

        Snackbar.make(graphicOverlay, R.string.ocr_screen_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_ok, {
                    ActivityCompat.requestPermissions(this@TextCaptureActivity, permissions, RC_CANERA_PERMISSION)
                }).show()
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
                Toast.makeText(this, R.string.ocr_screen_not_enough_storage, Toast.LENGTH_LONG).show()
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

    override fun onResume() {
        super.onResume()
        cameraPreview.stop()
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
        cameraPreview.release()
    }

    override fun tag(): String {
        return TextCaptureActivity::class.java.name
    }

    override val presenterFactory: PresenterFactory<TextCapturePresenter>
        get() = object : PresenterFactory<TextCapturePresenter> {
            override fun create(): TextCapturePresenter {
                return TextCapturePresenter(this@TextCaptureActivity)
            }
        }
}