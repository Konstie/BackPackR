package com.app.backpackr.ui.sections.home

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.network.models.Place
import com.app.backpackr.helpers.IntentHelper
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.home.HomePresenter
import com.app.backpackr.presenters.home.HomeView
import com.app.backpackr.ui.sections.abs.BaseActivity
import io.realm.RealmResults
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class HomeActivity : BaseActivity<HomePresenter, HomeView>(), HomeView {
    private var twoPane = false
    private var homePresenter: HomePresenter? = null

    @Inject lateinit var okHttpClient: OkHttpClient
    @Inject lateinit var retrofit: Retrofit

    var placesAdapter: LocationsListAdapter? = null

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.btn_add_new_place) lateinit var buttonAdd: FloatingActionButton
    @BindView(R.id.location_list) lateinit var locationsListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_list)
        ButterKnife.bind(this)
        setSupportActionBar(toolbar)
        toolbar.title = title
        buttonAdd.setOnClickListener { onPhotoButtonClicked() }
    }

    private fun onPhotoButtonClicked() {
        startActivity(IntentHelper.createOcrCameraIntent(this@HomeActivity))
    }

    override fun onPresenterPrepared(presenter: HomePresenter) {
        this.homePresenter = presenter
        homePresenter?.onViewAttached(this)
        homePresenter?.fetchSavedLocations()
    }

    override fun onLocationsLoaded(savedLocations: RealmResults<Place>) {
        placesAdapter = LocationsListAdapter(this, savedLocations, locationClickListener)
    }

    private object locationClickListener : LocationItemClickListener {
        override fun onShowGoogleMapsClicked(currentPlace: Place) {
            // todo: open location screen scrolled to Google Maps view
        }
    }

    override fun onLocationsLoadingStopped() {
        // todo: stop progress bar
    }

    override fun onPermissionsGranted(requestCode: Int) {}

    override fun tag(): String {
        return HomeActivity::class.java.name
    }

    override val presenterFactory: PresenterFactory<HomePresenter>
        get() = object : PresenterFactory<HomePresenter> {
            override fun create(): HomePresenter {
                return HomePresenter(this@HomeActivity)
            }
        }
}