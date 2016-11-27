package com.app.backpackr.ui.views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.google.android.gms.vision.text.Text
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
        val rect = getProcessedRect()
        canvas.drawRect(rect, rectPaint)

        val textComponents = textBlock?.components ?: return
        for (currentString in textComponents) {
            val left = translateX(currentString.boundingBox.left.toFloat())
            val bottom = translateY(currentString.boundingBox.bottom.toFloat())
            canvas.drawText(currentString.value, left, bottom, textPaint)
        }
    }

    override fun contains(x: Float, y: Float): Boolean {
        val rect = getProcessedRect()
        return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y)
    }

    fun getProcessedRect() : RectF {
        val rect = RectF(textBlock?.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        return rect
    }
}