package com.app.backpackr.ui.sections.textcapture

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Camera
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
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
import com.app.backpackr.utils.Constants
import com.app.backpackr.utils.IntentHelper
import com.app.backpackr.utils.RuntimePermissionsHelper
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
import java.util.*

class TextCaptureActivity : BaseActivity<TextCapturePresenter, ITextCaptureView>(), ITextCaptureView, View.OnClickListener {
    private val RC_CAMERA_PERMISSION = 2
    private val RC_PLAY_SERVICES = 9001
    private val TEXT_BLOCK_OBJECT = "TEXT_BLOCK_OBJECT"
    private val TAG = TextCaptureActivity::class.java.simpleName

    private var presenter: TextCapturePresenter? = null
    private var gestureDetector: GestureDetector? = null
    private var textDetectorProcessor: OcrDetectorProcessor? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var textRecognizer: TextRecognizer? = null

    lateinit var cameraSource: CustomCameraSource
    lateinit @BindView(R.id.graphic_overlay) var graphicOverlay: GraphicOverlay<OcrGraphic>
    lateinit @BindView(R.id.camera_preview) var cameraPreview: CameraSourcePreview
    lateinit @BindView(R.id.button_proceed) var buttonProceed: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupFullScreen()
        setContentView(R.layout.activity_capture_text)
        ButterKnife.bind(this)

        presenter = TextCapturePresenter(this@TextCaptureActivity)
        presenter?.onViewAttached(this)

        val autoFocus = true
        val useFlash = false

        if (RuntimePermissionsHelper.checkCameraPermission(this@TextCaptureActivity)) {
            createCameraSource(autoFocus, useFlash)
        } else {
            requestAppPermissions(arrayOf(Manifest.permission.CAMERA), R.string.permission_warning, Constants.PermissionCodes.CAMERA_REQUEST_CODE)
        }

        gestureDetector = GestureDetector(this, captureGestureListener)
        scaleGestureDetector = ScaleGestureDetector(this, scaleListener)
        buttonProceed.setOnClickListener(this)
    }

    fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val context = applicationContext
        textRecognizer = TextRecognizer.Builder(context).build()
        textDetectorProcessor = OcrDetectorProcessor(graphicOverlay, object: TextDetectionListener {
            override fun onTextBlocksReceived(detectedItems: SparseArray<TextBlock>) {
                Log.d(TAG, "onTextBlocksReceived: " + detectedItems.size())
                presenter?.addDetections(detectedItems)
            }
        })
        textRecognizer?.setProcessor(textDetectorProcessor)

        if (textRecognizer?.isOperational == false) {
            Log.w(TAG, "Camera detector dependencies are not available")

            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.ocr_screen_not_enough_storage, Toast.LENGTH_LONG).show()
                Log.w(TAG, "Yout device has low storage")
            }
        } else {
            Log.d(TAG, "CameraSource initialization started!")
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
        if (RuntimePermissionsHelper.checkCameraPermission(this)) {
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

    override fun onPermissionsGranted(requestCode: Int) {
        if (requestCode != Constants.PermissionCodes.CAMERA_REQUEST_CODE) {
            Log.d(TAG, "Something has gone wrong with permission result: " + requestCode)
            return
        }
        Log.d(TAG, "Camera permission was granted!")
        val autoFocus = intent.getBooleanExtra(Constants.Keys.EXTRA_AUTO_FOCUS, false)
        val useFlash = intent.getBooleanExtra(Constants.Keys.EXTRA_USE_FLASH, false)
        createCameraSource(autoFocus, useFlash)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val scaleGestureDetectorReady = scaleGestureDetector?.onTouchEvent(event)
        val gestureDetectorReady = gestureDetector?.onTouchEvent(event)
        return scaleGestureDetectorReady?:false || gestureDetectorReady?:false || super.onTouchEvent(event)
    }

    override fun onCapturedDataInitialized(capturedLabels: ArrayList<String>?) {
        startActivity(IntentHelper.createLoadingIntent(this, capturedLabels ?: ArrayList<String>()))
        finish()
    }

    override fun onPresenterPrepared(presenter: TextCapturePresenter) {
        this@TextCaptureActivity.presenter = presenter
        this@TextCaptureActivity.presenter?.onViewAttached(this)
    }

    override fun onDestroy() {
        textDetectorProcessor?.textDetectionListener = null
        cameraPreview.release()
        super.onDestroy()
    }

    override val presenterFactory: PresenterFactory<TextCapturePresenter>
        get() = object : PresenterFactory<TextCapturePresenter> {
            override fun create(): TextCapturePresenter {
                return TextCapturePresenter(this@TextCaptureActivity)
            }
        }
}