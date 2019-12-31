package org.wit.hillfort.startscreen

import android.app.Activity
import android.os.Handler
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW

class StartScreenPresenter(view: BaseView) : BasePresenter(view) {

    // Timer da splash screen
    private val startscreen_timer = 2000L

    fun doSplashscreen(){
        Handler().postDelayed(
            /*
            * Exibindo splash com um timer.
            */
            {
                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal
                app.hillforts.clear()
                view?.navigateTo(VIEW.LOGIN)
            }, startscreen_timer
        )
    }


}