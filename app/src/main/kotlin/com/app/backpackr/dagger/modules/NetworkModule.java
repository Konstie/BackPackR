package com.app.backpackr.dagger.modules;

import com.squareup.moshi.Moshi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

@Module
public class NetworkModule {
    private String baseUrl;

    public NetworkModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    public Moshi provideMoshi() {
        return new Moshi.Builder().build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttp() {
        return new OkHttpClient.Builder().build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(Moshi moshi, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }
}
