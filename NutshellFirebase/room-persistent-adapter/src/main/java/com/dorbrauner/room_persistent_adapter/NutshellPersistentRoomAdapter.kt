package com.dorbrauner.room_persistent_adapter

import android.app.Application
import androidx.room.Room
import com.dorbrauner.persistentadapters.PersistentAdapterContract


object NutshellPersistentRoomAdapter {

    fun build(application: Application): PersistentAdapterContract.Adapter {
        val database = Room.databaseBuilder(application, Database::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

        return RoomAdapter(PersistNotificationMessageToRoomNotificationMessageConverter(),
                           database.notificationMessageDao())
    }
}