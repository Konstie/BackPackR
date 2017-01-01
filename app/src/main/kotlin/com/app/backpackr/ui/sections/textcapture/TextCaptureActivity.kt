package com.app.backpackr.ui.sections.textcapture

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.SparseArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.helpers.Constants
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.textcapture.ITextCaptureView
import com.app.backpackr.presenters.textcapture.TextCapturePresenter
import com.app.backpackr.textprocessor.OcrDetectorProcessor
import com.app.backpackr.textprocessor.TextDetectionListener
import com.app.backpackr.ui.sections.abs.BaseActivity
import com.app.backpackr.ui.views.CameraSourcePreview
import com.app.backpackr.ui.views.CustomCameraSource
import com.app.backpackr.ui.views.GraphicOverlay
import com.app.backpackr.ui.views.OcrGraphic
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import java.io.IOException

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class TextCaptureActivity : BaseActivity<TextCapturePresenter, ITextCaptureView>(), ITextCaptureView, View.OnClickListener {
    val RC_CAMERA_PERMISSION = 2
    val RC_PLAY_SERVICES = 9001
    val TEXT_BLOCK_OBJECT = "TEXT_BLOCK_OBJECT"

    var presenter: TextCapturePresenter? = null
    var gestureDetector: GestureDetector? = null
    var textDetectorProcessor: OcrDetectorProcessor? = null
    var scaleGestureDetector: ScaleGestureDetector? = null
    var textRecognizer: TextRecognizer? = null

    lateinit var cameraSource: CustomCameraSource
    lateinit @BindView(R.id.graphic_overlay) var graphicOverlay: GraphicOverlay<OcrGraphic>
    lateinit @BindView(R.id.camera_preview) var cameraPreview: CameraSourcePreview
    lateinit @BindView(R.id.button_proceed) var buttonProceed: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_text)
        ButterKnife.bind(this)

        presenter = TextCapturePresenter(this@TextCaptureActivity)
        presenter?.onViewAttached(this)

        val autoFocus = true
        val useFlash = false

        val cameraPermissionStatus = cameraPermissionStatus()
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash)
        } else {
            requestCameraPermission()
        }

        gestureDetector = GestureDetector(this, captureGestureListener)
        scaleGestureDetector = ScaleGestureDetector(this, scaleListener)
    }

    fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val context = applicationContext
        textRecognizer = TextRecognizer.Builder(context).build()
        textDetectorProcessor = OcrDetectorProcessor(graphicOverlay, object: TextDetectionListener {
            override fun onTextBlocksReceived(detectedItems: SparseArray<TextBlock>) {
                presenter?.addDetections(detectedItems)
            }
        })
        textRecognizer?.setProcessor(textDetectorProcessor)

        if (textRecognizer?.isOperational == false) {
            Log.w(tag(), "Camera detector dependencies are not available")

            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.ocr_screen_not_enough_storage, Toast.LENGTH_LONG).show()
                Log.w(tag(), "Yout device has low storage")
            }
        } else {
            Log.d(tag(), "CameraSource initialization started!")
            cameraSource = CustomCameraSource.Builder(applicationContext, textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(15.0f)
                    .setFlashMode(if (useFlash) Camera.Parameters.FLASH_MODE_TORCH else null)
                    .setFocusMode(if (autoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE else null)
                    .build()
        }
    }


    private fun startCameraSource() {
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_CAMERA_PERMISSION)
            dialog.show()
        }
        try {
            cameraPreview.start(cameraSource, graphicOverlay)
        } catch (exc: IOException) {
            cameraSource.release()
        }
    }

    private fun requestCameraPermission() {
        Log.w(tag(), "requestCameraPermission called...")
        val permissions = arrayOf(Manifest.permission.CAMERA)
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_CAMERA_PERMISSION)
            return
        }

        Snackbar.make(graphicOverlay, R.string.ocr_screen_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_ok, {
                    ActivityCompat.requestPermissions(this@TextCaptureActivity, permissions, RC_CAMERA_PERMISSION)
                }).show()
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
        if (cameraPermissionStatus() == PackageManager.PERMISSION_GRANTED) {
            startCameraSource()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraPreview.stop()
    }

    override fun onClick(view: View?) {
        if (view == buttonProceed) {
            presenter?.onProcessCurrentImagePressed()
        }
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

        override fun onScaleEnd(detector: ScaleGestureDetector?) {
            cameraSource.doZoom(detector?.scaleFactor?: 0.0f)
        }

        override fun onScale(p0: ScaleGestureDetector?): Boolean {
            return false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode != RC_CAMERA_PERMISSION) {
            Log.d(tag(), "Something has gone wrong with permission result: " + requestCode)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(tag(), "Camera permission was granted!")
            val autoFocus = intent.getBooleanExtra(Constants.EXTRA_AUTO_FOCUS, false)
            val useFlash = intent.getBooleanExtra(Constants.EXTRA_USE_FLASH, false)
            createCameraSource(autoFocus, useFlash)
            return
        }
        Log.d(tag(), "Permission was not granted!")
        val dialogCallback = DialogInterface.OnClickListener { dialogInterface, i ->
            finish()
        }

        val dialogBuilder = AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.ocr_screen_permission_title)
                .setMessage(R.string.ocr_screen_permission_rationale)
                .setPositiveButton(R.string.action_ok, dialogCallback)
                .show()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val scaleGestureDetectorReady = scaleGestureDetector?.onTouchEvent(event)
        val gestureDetectorReady = gestureDetector?.onTouchEvent(event)
        return scaleGestureDetectorReady?:false || gestureDetectorReady?:false || super.onTouchEvent(event)
    }

    private fun cameraPermissionStatus(): Int {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    }

    override fun onPresenterPrepared(presenter: TextCapturePresenter) {
        this@TextCaptureActivity.presenter = presenter
        this@TextCaptureActivity.presenter?.onViewAttached(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        textDetectorProcessor?.textDetectionListener = null
        cameraPreview.release()
        presenter?.onViewDetached()
        presenter?.onDestroyed()
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