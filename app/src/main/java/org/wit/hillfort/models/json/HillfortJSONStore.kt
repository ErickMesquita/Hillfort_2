package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long {
  return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger {

  val context: Context
  var hillforts = mutableListOf<HillfortModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, JSON_FILE)) {
      deserialize()
    } else {
      refresh()
    }
  }

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
    hillfort.id = generateRandomId()
    hillforts.add(hillfort)
    serialize()
  }

  override fun update(hillfort: HillfortModel) {
    val hillfortsList = findAll(false) as ArrayList<HillfortModel>
    var foundHillfort: HillfortModel? = hillfortsList.find { p -> p.id == hillfort.id }
    if (foundHillfort != null) {
      foundHillfort.id = hillfort.id
      foundHillfort.fbId = hillfort.fbId
      foundHillfort.title = hillfort.title
      foundHillfort.description = hillfort.description
      foundHillfort.assigned = hillfort.assigned
      foundHillfort.visited = hillfort.visited
      foundHillfort.dateVisited = hillfort.dateVisited
      foundHillfort.image = hillfort.image
      foundHillfort.additionalNotes = hillfort.additionalNotes
      foundHillfort.createdBy = hillfort.createdBy
      foundHillfort.location = hillfort.location
    }
    serialize()
  }

  override fun delete(hillfort: HillfortModel) {
    hillforts.remove(hillforts.find{p -> p.id == hillfort.id})
    serialize()
  }

  override fun findById(id:Long) : HillfortModel? {
    return hillforts.find { it.id == id }
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(hillforts, listType)
    write(context, JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_FILE)
    hillforts = Gson().fromJson(jsonString, listType)
  }

  override fun refresh() {
    val jsonString: String = context.assets.open("original_hillforts.json").bufferedReader().use { it.readText() }
    try {
      hillforts = Gson().fromJson(jsonString, listType)
    }
    catch (e: Exception) {
      println(e)
    }
    serialize()
  }

  override fun clear() {
    hillforts.clear()
  }
}
