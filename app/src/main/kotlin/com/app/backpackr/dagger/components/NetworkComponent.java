package com.app.backpackr.dagger.components;

import com.app.backpackr.dagger.modules.AppModule;
import com.app.backpackr.dagger.modules.NetworkModule;
import com.app.backpackr.presenters.home.HomePresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by kmikhailovskiy on 24.11.2016.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface NetworkComponent {
    void inject(HomePresenter homePresenter);
}
