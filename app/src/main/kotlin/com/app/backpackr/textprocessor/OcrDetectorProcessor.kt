package com.app.backpackr.textprocessor

import android.util.Log
import android.util.SparseArray
import com.app.backpackr.ui.views.GraphicOverlay
import com.app.backpackr.ui.views.OcrGraphic
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

class OcrDetectorProcessor(var graphicOverlay: GraphicOverlay<OcrGraphic>, var textDetectionListener: TextDetectionListener? = null) : Detector.Processor<TextBlock> {
    val TAG = OcrDetectorProcessor::class.java.name

    override fun receiveDetections(detections: Detector.Detections<TextBlock>?) {
        graphicOverlay.clear()
        val textBlocks = detections?.detectedItems
        val textBlocksCount = textBlocks?.size() ?: 0
        for (i in 0..textBlocksCount) {
            val item = textBlocks?.valueAt(i)
            Log.d(TAG, "Text item: " + item?.value)
            val graphic = OcrGraphic(graphicOverlay, item)
            graphicOverlay.add(graphic)
        }
        if (textBlocks != null && textBlocks.size() > 0) {
            textDetectionListener?.onTextBlocksReceived(textBlocks)
        } else {
            Log.w(TAG, "No text blocks were received")
        }
    }

    override fun release() {
        graphicOverlay.clear()
    }
}

interface TextDetectionListener {
    fun onTextBlocksReceived(detectedItems: SparseArray<TextBlock>)
}