package com.app.backpackr.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.presenters.abs.BaseView
import com.app.backpackr.presenters.abs.Presenter
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.abs.PresenterLoader
import com.app.backpackr.presenters.home.HomePresenter
import com.app.backpackr.presenters.home.HomeView

/**
 * Created by konstie on 13.11.16.
 */
class HomeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Presenter<BaseView>>, HomeView {
    val LOADER_ID = 101
    var twoPane = false
    var presenter : HomePresenter? = null

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.btn_add_new_place) lateinit var buttonAdd: FloatingActionButton
    @BindView(R.id.location_list) lateinit var locationsListView: RecyclerView

    override fun onCreate(savedState: Bundle?) {
        setContentView(R.layout.activity_location_list)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        toolbar.setTitle(title)

        presenter = HomePresenter(this)
        presenter?.onViewAttached(this)

        buttonAdd.setOnClickListener {
            view -> {
                /* open photo activity */
            }
        }

        supportLoaderManager.initLoader(LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Presenter<BaseView>> {
        return PresenterLoader(object : PresenterFactory<HomeView, HomePresenter> {
            override fun create(): HomePresenter {
                return HomePresenter(this@HomeActivity)
            }
        }, presenter, this) as Loader<Presenter<BaseView>>
    }

    override fun onLoadFinished(loader: Loader<Presenter<BaseView>>?, cachedPresenter: Presenter<BaseView>?) {
        presenter = cachedPresenter as HomePresenter
    }

    override fun onLoaderReset(loader: Loader<Presenter<BaseView>>?) {
        presenter = null
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onViewDetached()
    }
}