package com.app.backpackr.presenters.textcapture

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import com.app.backpackr.helpers.Constants
import com.app.backpackr.presenters.abs.Presenter
import com.app.backpackr.textprocessor.services.PlacesRecognitionService
import com.google.android.gms.vision.text.TextBlock
import java.util.*

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class TextCapturePresenter(val context: Context) : Presenter<ITextCaptureView> {
    private var view: ITextCaptureView? = null
    private var textDetectionsList: ArrayList<String>? = null

    init {
        textDetectionsList = ArrayList()
    }

    override fun onViewAttached(view: ITextCaptureView) {
        this.view = view
    }

    fun addDetections(detections: SparseArray<TextBlock>) {
        textDetectionsList = convertDetectionsArrayToList(detections)
    }

    fun onProcessCurrentImagePressed() {
        val signsProcessingIntent = Intent(context, PlacesRecognitionService::class.java)
        signsProcessingIntent.putStringArrayListExtra(Constants.EXTRA_CAPTURED_SIGNS, textDetectionsList)
        context.startService(signsProcessingIntent)
    }

    fun convertDetectionsArrayToList(textDetections: SparseArray<TextBlock>): ArrayList<String> {
        val detectionsList = ArrayList<String>()
        for (index in 0..textDetections.size()) {
            detectionsList.add(textDetections[index].value)
        }
        return detectionsList
    }

    override fun onViewDetached() {
        this.view = null
    }

    override fun onDestroyed() {
    }
}