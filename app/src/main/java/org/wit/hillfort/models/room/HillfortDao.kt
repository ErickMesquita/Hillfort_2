package org.wit.hillfort.models.room

import androidx.room.*
import org.wit.hillfort.models.HillfortModel

@Dao
interface HillfortDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(hillfort: HillfortModel)

  @Query("SELECT * FROM HillfortModel WHERE assigned = :assignedOnly")
  fun findAll(assignedOnly: Boolean): List<HillfortModel>

  @Query("SELECT * FROM HillfortModel WHERE (assigned = :assignedOnly AND visited = :visitedOnly)")
  fun findAll(assignedOnly: Boolean, visitedOnly: Boolean): List<HillfortModel>

  @Query("select * from HillfortModel where id = :id")
  fun findById(id: Long): HillfortModel

  @Update
  fun update(hillfort: HillfortModel)

  @Delete
  fun deleteHillfort(hillfort: HillfortModel)
}