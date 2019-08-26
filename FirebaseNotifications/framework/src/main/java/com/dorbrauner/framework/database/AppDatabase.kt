package com.dorbrauner.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dorbrauner.framework.database.converters.DateTimeConverter
import com.here.driverapp.notifications.framework.MapConverter
import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.database.model.NotificationMessageDao

const val DATABASE_NAME = "notifications_db"
@Database(entities = [NotificationsFrameworkContract.Repository.NotificationMessage::class], version = 2)
@TypeConverters(
        DateTimeConverter::class,
        MapConverter::class
)
internal abstract class AppDatabase: RoomDatabase() {
    abstract fun notificationMessageDao(): NotificationMessageDao
}