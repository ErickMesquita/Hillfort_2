package org.wit.hillfort.views.hillfort

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class HillfortView : BaseView(), AnkoLogger {

  lateinit var presenter: HillfortPresenter
  var hillfort = HillfortModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_hillfort)
    super.init(toolbarAdd, true)

    presenter = initPresenter (HillfortPresenter(this)) as HillfortPresenter

    mapView.onCreate(savedInstanceState)
    mapView.getMapAsync {
      presenter.doConfigureMap(it)
      it.setOnMapClickListener { presenter.doUpdate(hillfort); presenter.doSetLocation() }
    }

    chooseImage.setOnClickListener { presenter.doUpdate(hillfort); presenter.doSelectNewImage() }
    hillfortImage0.setOnClickListener { presenter.doSelectImage(0) }
    hillfortImage1.setOnClickListener { presenter.doSelectImage(1) }
    hillfortImage2.setOnClickListener { presenter.doSelectImage(2) }
    hillfortImage3.setOnClickListener { presenter.doSelectImage(3) }


    switch_visited.setOnCheckedChangeListener { buttonView, isChecked ->
      if(isChecked){
        val c = Timestamp(hillfort.dateVisited)

        var year = c.year + 1900
        var month = c.month
        var day = c.date

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

          hillfort.dateVisited = GregorianCalendar(year, monthOfYear, dayOfMonth).timeInMillis
          info("$dayOfMonth - ${monthOfYear+1} - $year -> ${hillfort.dateVisited}")
          lblDate.setText(SimpleDateFormat("dd/MM/yyyy").format(Timestamp(hillfort.dateVisited)))


        }, year, month, day)
        dpd.show()
        lblDate.visibility = View.VISIBLE
      }
      else
        lblDate.visibility = View.GONE
    }

/*
    if (hillfort.image.size > 1) {
      hillfortImage2.visibility = VISIBLE
    }else {
      hillfortImage2.visibility = GONE
    }
    if (hillfort.image.size > 2) {
      hillfortImage3.visibility = VISIBLE
    }else {
      hillfortImage3.visibility = GONE
    }*/
  }

  override fun showHillfort(hillfort: HillfortModel) {
    hillfortTitle.setText(hillfort.title)
    description.setText(hillfort.description)

    lat.setText("lat: "+hillfort.location.lat.toString())
    lng.setText("lng: "+hillfort.location.lng.toString())
    switch_assigned.isChecked = hillfort.assigned

    switch_visited.setChecked(hillfort.visited)

    lblDate.visibility = if (hillfort.visited) VISIBLE else GONE
    lblDate.setText(SimpleDateFormat("dd/MM/yyyy").format(Timestamp(hillfort.dateVisited)))

    hillfortImage0.setImageBitmap(readImageFromPath(this, if (hillfort.image.size>0) hillfort.image.first() else ""))
    if (hillfort.image.size > 1) {
      hillfortImage1.visibility = VISIBLE
      hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.image[1]))
    }else{
      hillfortImage1.visibility = GONE
    }
    if (hillfort.image.size > 2) {
      hillfortImage2.visibility = VISIBLE
      hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.image[2]))
    }else{
      hillfortImage2.visibility = GONE
    }
    if (hillfort.image.size > 3) {
      hillfortImage3.visibility = VISIBLE
      hillfortImage3.setImageBitmap(readImageFromPath(this, hillfort.image[3]))
    }else{
      hillfortImage3.visibility = GONE
    }
    this.showLocation(hillfort.location.lat, hillfort.location.lng)
  }

  override fun showLocation(latitude : Double, longitude : Double) {
    lat.setText("%.6f".format(latitude))
    lng.setText("%.6f".format(longitude))
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_hillfort, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.item_save -> {
        if (hillfortTitle.text.toString().isEmpty()) {
          toast(R.string.enter_hillfort_title)
        } else {
          presenter.doAddOrSave(hillfortTitle.text.toString(), description.text.toString(), switch_assigned.isChecked, switch_visited.isChecked, hillfort)
        }
      }
      R.id.item_delete -> {
        presenter.doDelete()
      }
      R.id.item_cancel -> {
        presenter.doCancel()
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (data != null) {
      presenter.doActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    presenter.doCancel()
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
    presenter.doResartLocationUpdates()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }
}


