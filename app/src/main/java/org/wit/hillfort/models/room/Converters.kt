package org.wit.hillfort.models.room

import androidx.room.TypeConverter
import java.time.*

class Converters {
    @TypeConverter
    fun toZonedDateTime(value: Long?): ZonedDateTime {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(value ?: ZonedDateTime.of(2019, 10, 1, 0, 0, 0, 0, ZoneId.of("Z")).toEpochSecond()), ZoneId.of("Z"))
    }

    @TypeConverter
    fun fromZonedDateTime(date: ZonedDateTime? ): Long {
        return date?.toEpochSecond() ?: ZonedDateTime.of(2019, 10, 1, 0, 0, 0, 0, ZoneId.of("Z")).toEpochSecond()
    }

    @TypeConverter
    fun toList(value: String?): List<String>{
        return value?.split(";") ?: listOf<String>("")
    }

    @TypeConverter
    fun fromList(value: List<String>?): String{
        return value?.joinToString(";") ?: ""
    }
}