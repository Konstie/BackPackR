package com.app.backpackr.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.home.HomePresenter
import com.app.backpackr.presenters.home.HomeView
import com.app.backpackr.ui.abs.BaseActivity

/**
 * Created by konstie on 13.11.16.
 */
class HomeActivity(override val presenterFactory: PresenterFactory<HomePresenter>) : BaseActivity<HomePresenter, HomeView>(), HomeView {


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
    }

    override fun tag(): String {
        return HomeActivity::class.java.name
    }

    override fun onPresenterPrepared(presenter: HomePresenter) {
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onViewDetached()
    }
}