package com.dorbrauner.room_persistent_adapter.converters

import androidx.room.TypeConverter
import java.util.*

class DateTimeConverter {
    @TypeConverter
    fun dateTimeToMillis(dataTime: Date): Long = dataTime.time

    @TypeConverter
    fun millisToDateTime(value: Long): Date = Date(value)
}