package org.wit.hillfort.models.mem

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

var lastId = 0L

internal fun getId(): Long {
  return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger {

  val hillforts = ArrayList<HillfortModel>()

  override fun findAll(assignedOnly: Boolean): List<HillfortModel> {
    return if (assignedOnly){
      hillforts.filter { p -> p.assigned }
    }else
      hillforts
  }

  override fun findAll(assignedOnly: Boolean, visitedOnly: Boolean): List<HillfortModel> {
    return when{
      visitedOnly  and  assignedOnly -> hillforts.filter { p ->  p.visited and  p.assigned}
      !visitedOnly and  assignedOnly -> hillforts.filter { p ->                 p.assigned}
      visitedOnly  and !assignedOnly -> hillforts.filter { p ->  p.visited                }
      else -> hillforts
    }
  }

  override fun create(hillfort: HillfortModel) {
    hillfort.id = getId()
    hillforts.add(hillfort)
    logAll()
  }

  override fun update(hillfort: HillfortModel) {
    var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
    if (foundHillfort != null) {
      foundHillfort.id = hillfort.id
      foundHillfort.title = hillfort.title
      foundHillfort.fbId = hillfort.fbId
      foundHillfort.description = hillfort.description
      foundHillfort.image = hillfort.image
      foundHillfort.location = hillfort.location
      logAll();
    }
  }

  override fun delete(hillfort: HillfortModel) {
    hillforts.remove(hillfort)
  }

  override fun findById(id:Long) : HillfortModel? {
    val foundHillfort: HillfortModel? = hillforts.find { it.id == id }
    return foundHillfort
  }

  fun logAll() {
    hillforts.forEach { info("${it}") }
  }

  override fun clear() {
    hillforts.clear()
  }
}