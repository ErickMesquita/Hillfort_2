package org.wit.hillfort.models.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.HillfortStore

class HillfortFireStore(val context: Context) : HillfortStore, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

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

    override fun findById(id: Long): HillfortModel? {
        return hillforts.find { p -> p.id == id }
    }

    override fun create(hillfort: HillfortModel) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillfort.fbId = key
            hillfort.createdBy = FirebaseAuth.getInstance().currentUser?.displayName.toString()
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
        }
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
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

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)

    }

    override fun delete(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        Log.d(null, "userId: $userId")
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }
}