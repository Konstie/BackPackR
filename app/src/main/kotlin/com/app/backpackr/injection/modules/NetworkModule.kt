package com.app.backpackr.injection.modules

import com.app.backpackr.data.network.ApiBaseUrls
import com.app.backpackr.data.network.ApiHeaders
import com.app.backpackr.data.network.LoggingInterceptor
import com.app.backpackr.data.network.models.BaseUrl
import com.squareup.moshi.Moshi

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

@Module
class NetworkModule {
    val CONNECT_TIMEOUT: Long = 30

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideBaseUrl(): BaseUrl {
        return BaseUrl(ApiBaseUrls.GOOGLE_PLACES.toString())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(LoggingInterceptor())

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: BaseUrl, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl.endPointUrl)
                .client(okHttpClient)
                .build()
    }
}
