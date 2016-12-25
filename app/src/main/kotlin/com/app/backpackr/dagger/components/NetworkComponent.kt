package com.app.backpackr.dagger.components

import com.app.backpackr.api.models.BaseUrl
import com.app.backpackr.dagger.modules.NetworkModule
import com.app.backpackr.presenters.textcapture.TextCapturePresenter
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */
@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {
    fun baseUrl(): BaseUrl
    fun okHttpClient(): OkHttpClient
    fun retrofit(): Retrofit
    fun inject(textCapturePresenter: TextCapturePresenter)
}