package org.wit.hillfort.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Parcelize
@Entity
data class HillfortModel(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                         var fbId : String = "",
                         var title: String = "",
                         var description: String = "",
                         var assigned: Boolean = true,
                         var visited: Boolean = false,
                         //var dateVisited: java.sql.Timestamp = Timestamp(1539918000000),
                         //var dateVisited: ZonedDateTime = ZonedDateTime.of(2019, 10, 1, 0, 0, 0, 0, ZoneId.of("Z")),
                         var dateVisited: Long = 1539918000000,
                         var image: MutableList<String> = mutableListOf<String>(),
                         var additionalNotes: String = "",
                         var createdBy: String = "",
                         @Embedded var location: Location = Location()) : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable


