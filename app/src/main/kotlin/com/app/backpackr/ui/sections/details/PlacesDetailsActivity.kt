package com.app.backpackr.ui.sections.details

import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.details.DetailsPresenter
import com.app.backpackr.presenters.details.IDetailsView
import com.app.backpackr.ui.sections.abs.BaseActivity

class PlacesDetailsActivity : BaseActivity<DetailsPresenter, IDetailsView>(), IDetailsView {
    private var detailsPresenter: DetailsPresenter? = null

    override fun onPresenterPrepared(presenter: DetailsPresenter) {
        detailsPresenter = presenter
        detailsPresenter?.onViewAttached(this@PlacesDetailsActivity)
    }

    override fun tag(): String {
        return PlacesDetailsActivity::class.java.simpleName
    }

    override val presenterFactory: PresenterFactory<DetailsPresenter>
        get() = object : PresenterFactory<DetailsPresenter> {
            override fun create(): DetailsPresenter {
                return DetailsPresenter()
            }
        }

    override fun onPermissionsGranted(requestCode: Int) {}
}