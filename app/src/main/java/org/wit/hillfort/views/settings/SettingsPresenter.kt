package org.wit.hillfort.views.settings


import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.views.BasePresenter
import org.wit.hillfort.views.BaseView
import org.wit.hillfort.views.VIEW
import org.jetbrains.anko.toast

class SettingsPresenter(view: BaseView) : BasePresenter(view), AnkoLogger {

    fun doSetTexts(){
        var auth: FirebaseAuth = FirebaseAuth.getInstance()
        view!!.email.setText("Email: ${auth.currentUser}")
        view!!.globalHillfortsNumber.setText("Global number of Hillforts:   ${app.hillforts.findAll(false).count()}")
        view!!.assignedHillfortsNumber.setText("Number of Hillforts Assigned: ${app.hillforts.findAll(assignedOnly = true).count()}")
        view!!.visitedHillfortsNumber.setText("Visited Hillforts: ${app.hillforts.findAll(assignedOnly = false, visitedOnly = true).count()}")
    }

    fun doSetButtons(){
        view!!.refresh.setOnClickListener {
            app.hillforts.refresh()
            view?.toast(R.string.refreshed)
        }
        view!!.logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            app.hillforts.clear()
            view?.navigateTo(VIEW.LOGIN)
        }
    }
}