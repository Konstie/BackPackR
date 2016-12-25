package com.app.backpackr.dagger.modules

import com.app.backpackr.api.ApiBaseUrls
import com.app.backpackr.api.ApiHeaders
import com.app.backpackr.api.models.BaseUrl
import com.squareup.moshi.Moshi

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

@Module
class NetworkModule() {
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
                .addInterceptor {
                    val originalRequest = it.request()
                    val requestBuilder = originalRequest.newBuilder()
                            .addHeader(ApiHeaders.KEY_ACCEPT, ApiHeaders.VAL_CONTENT_TYPE)
                            .addHeader(ApiHeaders.KEY_CONTENT_TYPE, ApiHeaders.VAL_CONTENT_TYPE)
                    val request = requestBuilder.build()
                    it.proceed(request)
                }

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: BaseUrl, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(baseUrl.endPointUrl)
                .client(okHttpClient)
                .build()
    }
}
