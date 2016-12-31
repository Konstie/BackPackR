package com.app.backpackr.presenters.textcapture

import android.content.Context
import android.util.SparseArray
import com.app.backpackr.presenters.abs.Presenter
import com.google.android.gms.vision.text.TextBlock

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class TextCapturePresenter(context: Context) : Presenter<ITextCaptureView> {
    private var view: ITextCaptureView? = null
    private var textDetectionsArray: SparseArray<TextBlock>? = null

    init {
        textDetectionsArray = SparseArray()
    }

    override fun onViewAttached(view: ITextCaptureView) {
        this.view = view
    }

    fun addDetections(detections: SparseArray<TextBlock>) {
        textDetectionsArray = detections
    }

    fun onProcessCurrentImagePressed() {
        // todo: start text processing service
    }

    override fun onViewDetached() {
        this.view = null
    }

    override fun onDestroyed() {
    }
}