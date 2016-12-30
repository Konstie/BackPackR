package com.app.backpackr.ui.views

import android.hardware.Camera
import android.support.annotation.StringDef
import java.lang.annotation.RetentionPolicy

/**
 * Created by konstie on 29.12.16.
 */
@StringDef(
        Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
        Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,
        Camera.Parameters.FOCUS_MODE_EDOF,
        Camera.Parameters.FOCUS_MODE_AUTO,
        Camera.Parameters.FOCUS_MODE_FIXED,
        Camera.Parameters.FOCUS_MODE_INFINITY,
        Camera.Parameters.FOCUS_MODE_MACRO)
@Retention(AnnotationRetention.SOURCE)
annotation class FocusMode