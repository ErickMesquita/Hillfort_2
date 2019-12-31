package org.wit.hillfort.views.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_settings.*
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class SettingsView  :  BaseView(){

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        super.init(toolbar_activity_settings, false)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        toolbar_activity_settings.title = title
        setSupportActionBar(toolbar_activity_settings)

        presenter.doSetTexts()

        presenter.doSetButtons()


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_save -> finish()
            R.id.item_cancel -> finish()
        }
        return super.onOptionsItemSelected(item!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }
}