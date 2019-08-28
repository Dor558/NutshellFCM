package com.dorbrauner.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dorbrauner.framework.database.converters.DateTimeConverter
import com.dorbrauner.framework.database.converters.MapConverter
import com.dorbrauner.framework.database.converters.NotificationTypeConverter
import com.dorbrauner.framework.database.dao.NotificationMessageDao
import com.dorbrauner.framework.database.model.NotificationMessage

const val DATABASE_NAME = "notifications_db"
@Database(entities = [NotificationMessage::class], version = 3)
@TypeConverters(
        DateTimeConverter::class,
        MapConverter::class,
        NotificationTypeConverter::class
)
internal abstract class Database: RoomDatabase() {
    abstract fun notificationMessageDao(): NotificationMessageDao
}