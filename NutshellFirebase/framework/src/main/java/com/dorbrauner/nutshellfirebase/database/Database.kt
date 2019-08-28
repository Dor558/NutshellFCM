package com.dorbrauner.nutshellfirebase.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dorbrauner.nutshellfirebase.database.converters.DateTimeConverter
import com.dorbrauner.nutshellfirebase.database.converters.MapConverter
import com.dorbrauner.nutshellfirebase.database.converters.NotificationTypeConverter
import com.dorbrauner.nutshellfirebase.database.dao.NotificationMessageDao
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage

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