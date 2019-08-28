package com.dorbrauner.framework.database.converters

import androidx.room.TypeConverter
import org.joda.time.DateTime

class DateTimeConverter {
    @TypeConverter
    fun dateTimeToMillis(dataTime: DateTime): Long = dataTime.millis

    @TypeConverter
    fun millisToDateTime(value: Long): DateTime = DateTime(value)
}