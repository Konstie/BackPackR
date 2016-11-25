package com.app.backpackr.presenters.abs

import android.content.Context
import com.app.backpackr.presenters.home.HomePresenter

/**
 * Created by kmikhailovskiy on 23.11.2016.
 */

class HomePresenterFactory(var context: Context, var title: String) : PresenterFactory<HomePresenter> {
    override fun create(): HomePresenter {
        return HomePresenter(context)
    }
}