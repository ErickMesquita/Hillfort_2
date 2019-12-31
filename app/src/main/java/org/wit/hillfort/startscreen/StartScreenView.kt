package org.wit.hillfort.startscreen

import android.os.Bundle
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class StartScreenView :  BaseView() {

    lateinit var presenter: StartScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startscreen)

        presenter = initPresenter(StartScreenPresenter(this)) as StartScreenPresenter

        presenter.doSplashscreen()
    }

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.activity_startscreen)

        presenter = initPresenter(StartScreenPresenter(this)) as StartScreenPresenter

        presenter.doSplashscreen()
    }
}