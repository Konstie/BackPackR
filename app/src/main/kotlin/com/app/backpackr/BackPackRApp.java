package com.app.backpackr;

import android.app.Application;

import com.app.backpackr.api.ApiBaseUrls;
import com.app.backpackr.dagger.components.DaggerNetworkComponent;
import com.app.backpackr.dagger.components.NetworkComponent;
import com.app.backpackr.dagger.modules.AppModule;
import com.app.backpackr.dagger.modules.NetworkModule;

import javax.inject.Inject;

/**
 * Created by kmikhailovskiy on 25.11.2016.
 */

public class BackPackRApp extends Application {
    @Inject
    NetworkComponent networkComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        networkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(ApiBaseUrls.GOOGLE_PLACES.toString()))
                .build();
    }
}
