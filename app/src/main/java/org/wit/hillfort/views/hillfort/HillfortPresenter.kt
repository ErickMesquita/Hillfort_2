package org.wit.hillfort.views.hillfort

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.hillfort.helpers.checkLocationPermissions
import org.wit.hillfort.helpers.createDefaultLocationRequest
import org.wit.hillfort.helpers.isPermissionGranted
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.*

class HillfortPresenter(view: BaseView) : BasePresenter(view) {

  var map: GoogleMap? = null
  var hillfort = HillfortModel()
  var defaultLocation = Location(52.245696, -7.139102, 15f)
  var edit = false
  var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
  val locationRequest = createDefaultLocationRequest()

  init {
    if (view.intent.hasExtra("hillfort_edit")) {
      edit = true
      hillfort = view.intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
      view.showHillfort(hillfort)
    } else {
      if (checkLocationPermissions(view)) {
        doSetCurrentLocation()
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun doSetCurrentLocation() {
    locationService.lastLocation.addOnSuccessListener {
      locationUpdate(it?.latitude ?: 52.245696, it?.longitude ?: -7.139102)
    }
  }

  @SuppressLint("MissingPermission")
  fun doResartLocationUpdates() {
    var locationCallback = object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        if (locationResult != null && locationResult.locations != null) {
          val l = locationResult.locations.last()
          locationUpdate(l.latitude, l.longitude)
        }
      }
    }
    if (!edit) {
      locationService.requestLocationUpdates(locationRequest, locationCallback, null)
    }
  }


  override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    if (isPermissionGranted(requestCode, grantResults)) {
      doSetCurrentLocation()
    } else {
      locationUpdate(defaultLocation.lat, defaultLocation.lng)
    }
  }

  fun doConfigureMap(m: GoogleMap) {
    map = m
    locationUpdate(hillfort.location.lat, hillfort.location.lng)
  }

  fun locationUpdate(lat: Double, lng: Double) {
    hillfort.location.lat = lat
    hillfort.location.lng = lng
    hillfort.location.zoom = 15f
    map?.clear()
    map?.uiSettings?.setZoomControlsEnabled(true)
    val options =
      MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
    map?.addMarker(options)
    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
    view?.showLocation(hillfort.location.lat, hillfort.location.lng)
  }


  fun doAddOrSave(title: String, description: String, assigned :Boolean, visited :Boolean, hillfortV: HillfortModel) {
    hillfort = hillfortV
    hillfort.title = title
    hillfort.description = description
    hillfort.assigned = assigned
    hillfort.visited = visited
    doAsync {
      if (edit) {
        app.hillforts.update(hillfort)
      } else {
        app.hillforts.create(hillfort)
      }
      uiThread {
        view?.finish()
      }
    }
  }

  fun doUpdate(hillfortV: HillfortModel){
    hillfort = hillfortV
  }

  fun doCancel() {
    view?.finish()
  }

  fun doDelete() {
    app.hillforts.delete(hillfort)
    view?.finish()
  }

  fun doSelectNewImage() {
    view?.let {
      showImagePicker(view!!, NEW_IMAGE_REQUEST)
    }
  }

  fun doSelectImage(n: Int) {
    view?.let {
      showImagePicker(view!!, IMAGE_REQUEST+n)
    }
  }

  fun doSetLocation() {
    view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.location.lat, hillfort.location.lng, hillfort.location.zoom))
  }

  override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
    when (requestCode) {
      NEW_IMAGE_REQUEST -> {
        hillfort.image.add(data.data.toString())
        view?.showHillfort(hillfort)
      }
      LOCATION_REQUEST -> {
        val location = data.extras?.getParcelable<Location>("location")!!
        hillfort.location = location
        locationUpdate(hillfort.location.lat, hillfort.location.lng)
      }
      else -> {
        hillfort.image[requestCode - IMAGE_REQUEST] = data.data.toString()
        view?.showHillfort(hillfort)
      }
    }
  }
}