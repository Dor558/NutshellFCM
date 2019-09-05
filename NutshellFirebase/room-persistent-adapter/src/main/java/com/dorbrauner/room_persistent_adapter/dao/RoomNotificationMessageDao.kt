package com.dorbrauner.room_persistent_adapter.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dorbrauner.persistentadapters.PersistentAdapterContract.*
import com.dorbrauner.room_persistent_adapter.RoomAdapterContract.Companion.ROOM_TABLE_NOTIFICATION_MESSAGE
import com.dorbrauner.room_persistent_adapter.model.RoomNotificationMessage

@Dao
interface RoomNotificationMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(persistNotificationMessage: RoomNotificationMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(persistNotificationMessages: List<RoomNotificationMessage>)

    @Query("SELECT * FROM $ROOM_TABLE_NOTIFICATION_MESSAGE")
    fun getAll(): List<RoomNotificationMessage>

    @Query("SELECT * FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id == :id")
    fun getById(id: String): RoomNotificationMessage

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE")
    fun deleteAll()

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id == :id")
    fun deleteByActionId(id: String)

    @Query("DELETE FROM $ROOM_TABLE_NOTIFICATION_MESSAGE WHERE action_id in (:list)")
    fun deleteGroup(list: List<String>)
}