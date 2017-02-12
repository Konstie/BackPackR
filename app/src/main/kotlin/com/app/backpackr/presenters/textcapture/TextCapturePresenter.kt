package com.app.backpackr.presenters.textcapture

import android.content.Context
import android.util.Log
import android.util.SparseArray
import com.app.backpackr.presenters.abs.Presenter
import com.google.android.gms.vision.text.TextBlock
import java.util.*

class TextCapturePresenter(val context: Context) : Presenter<ITextCaptureView> {
    private val TAG = TextCapturePresenter::class.java.simpleName

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
        Log.d(TAG, "Adding detections: " + textDetectionsList);
    }

    fun onProcessCurrentImagePressed() {
        Log.d(TAG, "onProcessCurrentImagePressed");
        view?.onCapturedDataInitialized(textDetectionsList)
    }

    fun convertDetectionsArrayToList(textDetections: SparseArray<TextBlock>): ArrayList<String> {
        val detectionsList = (0 until textDetections.size())
                        .mapTo(ArrayList<String>()) { if (textDetections[it] != null) textDetections[it].value else "" }
        return detectionsList
    }

    override fun onViewDetached() {
        this.view = null
    }

    override fun onDestroyed() {
    }
}