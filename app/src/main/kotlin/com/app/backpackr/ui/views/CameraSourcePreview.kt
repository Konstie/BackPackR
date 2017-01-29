package com.app.backpackr.ui.views

import android.Manifest
import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.ViewGroup
import com.google.android.gms.vision.CameraSource
import android.support.annotation.RequiresPermission
import java.io.IOException
import android.content.res.Configuration
import android.util.Log
import android.view.SurfaceHolder

class CameraSourcePreview(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {
    val TAG = "CameraSourcePreview"

    private var surfaceView: SurfaceView? = null
    private var startRequested: Boolean = false
    private var surfaceAvailable: Boolean = false
    private var cameraSource: CustomCameraSource? = null
    private var graphicOverlay: GraphicOverlay<OcrGraphic>? = null

    init {
        surfaceView = SurfaceView(context)
        surfaceView?.holder?.addCallback(SurfaceCallback())
        addView(surfaceView)
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class)
    fun start(cameraSource: CustomCameraSource?) {
        if (cameraSource == null) {
            stop()
        }

        this.cameraSource = cameraSource

        if (cameraSource != null) {
            startRequested = true
            startIfReady()
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class)
    fun start(cameraSource: CustomCameraSource, overlay: GraphicOverlay<OcrGraphic>) {
        graphicOverlay = overlay
        start(cameraSource)
    }

    fun stop() {
        cameraSource?.stop()
    }

    fun release() {
        cameraSource?.release()
        cameraSource = null
    }

    private inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surface: SurfaceHolder) {
            surfaceAvailable = true
            try {
                startIfReady()
            } catch (se: SecurityException) {
                Log.e(TAG, "Do not have permission to start the camera", se)
            } catch (e: IOException) {
                Log.e(TAG, "Could not start camera source.", e)
            }

        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            surfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class)
    private fun startIfReady() {
        if (startRequested && surfaceAvailable) {
            cameraSource?.start(surfaceView?.holder)
            if (graphicOverlay != null) {
                val size = cameraSource?.previewSize
                val min = Math.min(size?.width?: 0, size?.height?: 0)
                val max = Math.max(size?.width?: 0, size?.height?: 0)
                if (isPortraitMode()) {
                    // Swap width and height sizes when in portrait, since it will be rotated by
                    // 90 degrees
                    graphicOverlay?.setCameraInfo(min, max, cameraSource?.cameraFacing?: 0)
                } else {
                    graphicOverlay?.setCameraInfo(max, min, cameraSource?.cameraFacing?: 0)
                }
                graphicOverlay?.clear()
            }
            startRequested = false
        }
    }

    private fun isPortraitMode(): Boolean {
        val orientation = context.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true
        }

        Log.d(TAG, "isPortraitMode returning false by default")
        return false
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var previewWidth = 320
        var previewHeight = 240
        if (cameraSource != null) {
            val size = cameraSource?.previewSize
            if (size != null) {
                previewWidth = size.width
                previewHeight = size.height
            }
        }

        if (isPortraitMode()) {
            val tmp = previewWidth
            previewWidth = previewHeight
            previewHeight = tmp
        }

        val viewWidth = right - left
        val viewHeight = bottom - top

        val childWidth: Int
        val childHeight: Int
        var childXOffset = 0
        var childYOffset = 0
        val widthRatio = viewWidth.toFloat() / previewWidth.toFloat()
        val heightRatio = viewHeight.toFloat() / previewHeight.toFloat()

        if (widthRatio > heightRatio) {
            childWidth = viewWidth
            childHeight = (previewHeight.toFloat() * widthRatio).toInt()
            childYOffset = (childHeight - viewHeight) / 2
        } else {
            childWidth = (previewWidth.toFloat() * heightRatio).toInt()
            childHeight = viewHeight
            childXOffset = (childWidth - viewWidth) / 2
        }

        for (i in 0..childCount - 1) {
            getChildAt(i).layout(
                    -1 * childXOffset, -1 * childYOffset,
                    childWidth - childXOffset, childHeight - childYOffset)
        }

        try {
            startIfReady()
        } catch (se: SecurityException) {
            Log.e(TAG, "Do not have permission to start the camera", se)
        } catch (e: IOException) {
            Log.e(TAG, "Could not start camera source.", e)
        }

    }

}