package com.app.backpackr.presenters.textcapture

import java.util.*

interface ITextCaptureView {
    fun onCapturedDataInitialized(capturedLabels: ArrayList<String>?)
}