package com.dorbrauner.nutshellfirebase.database.converters

import androidx.room.TypeConverter
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.*


class NotificationTypeConverter {

    @TypeConverter
    fun toNotificationType(value: String): NotificationType {
        return NotificationType.values().find { it.value == value } ?: NotificationType.NOTIFICATION
    }

    @TypeConverter
    fun fromNotificationType(type: NotificationType): String {
        return type.value
    }

}