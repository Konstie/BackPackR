package com.app.backpackr.ui.views

import android.hardware.Camera
import android.support.annotation.StringDef

/**
 * Created by konstie on 29.12.16.
 */

@StringDef(
        Camera.Parameters.FLASH_MODE_ON,
        Camera.Parameters.FLASH_MODE_OFF,
        Camera.Parameters.FLASH_MODE_AUTO,
        Camera.Parameters.FLASH_MODE_RED_EYE,
        Camera.Parameters.FLASH_MODE_TORCH
)
@Retention(AnnotationRetention.SOURCE)
annotation class FlashMode