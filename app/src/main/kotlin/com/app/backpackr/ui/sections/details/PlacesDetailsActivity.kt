package com.app.backpackr.ui.sections.details

import android.os.Bundle
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.details.DetailsPresenter
import com.app.backpackr.presenters.details.IDetailsView
import com.app.backpackr.ui.sections.abs.BaseActivity

class PlacesDetailsActivity : BaseActivity<DetailsPresenter, IDetailsView>(), IDetailsView {
    private var detailsPresenter: DetailsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_details)
        ButterKnife.bind(this)
    }

    override fun onPresenterPrepared(presenter: DetailsPresenter) {
        detailsPresenter = presenter
        detailsPresenter?.onViewAttached(this@PlacesDetailsActivity)
    }

    override val presenterFactory: PresenterFactory<DetailsPresenter>
        get() = object : PresenterFactory<DetailsPresenter> {
            override fun create(): DetailsPresenter {
                return DetailsPresenter()
            }
        }

    override fun onPermissionsGranted(requestCode: Int) {}
}