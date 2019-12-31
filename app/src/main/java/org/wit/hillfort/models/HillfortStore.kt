package org.wit.hillfort.models

interface HillfortStore {
  fun findAll(assignedOnly: Boolean): List<HillfortModel>
  fun findAll(assignedOnly: Boolean, visitedOnly: Boolean): List<HillfortModel>
  fun create(hillfort: HillfortModel)
  fun update(hillfort: HillfortModel)
  fun delete(hillfort: HillfortModel)
  fun clear(){}
  fun refresh(){}
  fun findById(id:Long) : HillfortModel?
}