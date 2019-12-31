package org.wit.hillfort.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.room.HillfortDao
import androidx.room.TypeConverters
import org.wit.hillfort.models.room.Converters

@Database(entities = arrayOf(HillfortModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

  abstract fun hillfortDao(): HillfortDao
}