package com.nutshellfcm.room_persistent_adapter

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nutshellfcm.room_persistent_adapter.converters.DateTimeConverter
import com.nutshellfcm.room_persistent_adapter.converters.MapConverter
import com.nutshellfcm.room_persistent_adapter.dao.RoomNotificationMessageDao
import com.nutshellfcm.room_persistent_adapter.model.RoomNotificationMessage

const val DATABASE_NAME = "notifications_db"
@Database(entities = [RoomNotificationMessage::class], version = 3)
@TypeConverters(
        DateTimeConverter::class,
        MapConverter::class
)
internal abstract class Database: RoomDatabase() {
    abstract fun notificationMessageDao(): RoomNotificationMessageDao
}