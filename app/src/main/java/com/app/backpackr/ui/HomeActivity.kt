package com.app.backpackr.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import com.app.backpackr.R
import com.app.backpackr.presenters.BaseView
import com.app.backpackr.presenters.Presenter

/**
 * Created by konstie on 13.11.16.
 */
class HomeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Presenter<BaseView>> {
    var twoPane = false
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.btn_add_new_place) lateinit var buttonAdd: FloatingActionButton
    @BindView(R.id.location_list) lateinit var locationsListView: RecyclerView

    override fun onCreate(savedState: Bundle?) {
        setContentView(R.layout.activity_location_list)
        setSupportActionBar(toolbar)
        toolbar.setTitle(title)

        buttonAdd.setOnClickListener {
            view -> {
                /* open photo activity */
            }
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Presenter<BaseView>> {

    }

    override fun onLoadFinished(loader: Loader<Presenter<BaseView>>?, data: Presenter<BaseView>?) {

    }

    override fun onLoaderReset(loader: Loader<Presenter<BaseView>>?) {

    }
}