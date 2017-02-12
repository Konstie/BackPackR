package com.app.backpackr.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

class RuntimePermissionsHelper {
    companion object {
        fun checkFineLocationPermission(context: Context): Boolean {
            return checkPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        }

        fun checkCameraPermission(context: Context): Boolean {
            return checkPermission(context, Manifest.permission.CAMERA)
        }

        fun checkPermission(context: Context, permission: String): Boolean {
            try {
                return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            } catch (exc: RuntimeException) {
                return false
            }
        }
    }
}