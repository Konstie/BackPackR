package com.app.backpackr.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    private val TAG = LoggingInterceptor::class.java.simpleName

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()

        Log.i(TAG, String.format("Sending request by url %s with params %s", request?.url(), request?.body()))

        val response = chain.proceed(request)

        val endTime = System.currentTimeMillis()
        Log.d(TAG, String.format("Retrieved response for %s in %d ms", response?.request()?.url(), endTime - startTime))

        return response
    }
}