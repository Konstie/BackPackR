package com.app.backpackr.ui.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.android.gms.vision.text.TextBlock

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

class OcrGraphic(var overlay: GraphicOverlay<OcrGraphic>, var textBlock: TextBlock? = null) : GraphicOverlay.Graphic(overlay) {
    val TEXT_COLOR: Int = Color.WHITE

    var id: Int? = null
    var rectPaint: Paint? = null
    var textPaint: Paint? = null

    init {
        rectPaint = Paint()
        rectPaint?.color = TEXT_COLOR
        rectPaint?.style = Paint.Style.STROKE
        rectPaint?.strokeWidth = 4.0f

        textPaint = Paint()
        textPaint?.color = TEXT_COLOR
        textPaint?.textSize = 54.0f

        // redraw the overlay
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {

    }

    override fun contains(x: Float, y: Float): Boolean {
        return false
    }


}