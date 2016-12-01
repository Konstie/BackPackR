package com.app.backpackr.ui.sections.loading

import com.app.backpackr.presenters.abs.PresenterFactory
import com.app.backpackr.presenters.textcapture.ITextCaptureView
import com.app.backpackr.presenters.textcapture.TextCapturePresenter
import com.app.backpackr.ui.sections.abs.BaseActivity

/**
 * Created by kmikhailovskiy on 01.12.2016.
 */
class LoadingActivity : BaseActivity<TextCapturePresenter, ITextCaptureView>() {


    override fun onPresenterPrepared(presenter: TextCapturePresenter) {

    }

    override fun tag(): String {
        return LoadingActivity::class.java.name
    }

    override val presenterFactory: PresenterFactory<TextCapturePresenter>
        get() = object : PresenterFactory<TextCapturePresenter> {
            override fun create(): TextCapturePresenter {
                return TextCapturePresenter(this@LoadingActivity)
            }
        }
}