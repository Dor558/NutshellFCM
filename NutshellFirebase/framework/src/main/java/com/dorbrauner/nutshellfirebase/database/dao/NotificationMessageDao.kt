package com.dorbrauner.nutshellfirebase.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Sources.PersistentSource.Companion.ROOM_TABLE_NOTIFICATION_MESSAGE

@Dao
interface NotificationMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notificationMessage: NotificationMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(notificationMessages: List<NotificationMessage>)

    @Query("SELECT * FROM $ROOM_TABLE_NOTIFICATION_MESSAGE")
    fun getAll(): List<NotificationMessage>

    @Query("SELECT * FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id == :id")
    fun getById(id: String): NotificationMessage

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE")
    fun deleteAll()

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id == :id")
    fun deleteByActionId(id: String)

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id in (:list)")
    fun deleteGroup(list: List<String>)
}