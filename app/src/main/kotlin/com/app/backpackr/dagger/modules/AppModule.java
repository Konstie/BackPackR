package com.app.backpackr.dagger.modules;

import android.content.Context;

import com.app.backpackr.BackPackRApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

@Module
public class AppModule {
    private BackPackRApp application;

    public AppModule(BackPackRApp application) {
        this.application = application;
    }

    @Provides @Singleton
    public BackPackRApp provideApplication() {
        return application;
    }

    public Context getAppContext() {
        return application.getApplicationContext();
    }
}
