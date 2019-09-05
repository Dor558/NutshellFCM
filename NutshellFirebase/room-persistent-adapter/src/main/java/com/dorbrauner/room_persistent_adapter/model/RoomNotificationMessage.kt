package com.dorbrauner.room_persistent_adapter.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dorbrauner.persistentadapters.PersistentAdapterContract
import com.dorbrauner.room_persistent_adapter.RoomAdapterContract.Companion.KEY_ACTION_ID
import com.dorbrauner.room_persistent_adapter.RoomAdapterContract.Companion.KEY_PAYLOAD
import com.dorbrauner.room_persistent_adapter.RoomAdapterContract.Companion.KEY_TIMESTAMP
import com.dorbrauner.room_persistent_adapter.RoomAdapterContract.Companion.KEY_TYPE
import com.dorbrauner.room_persistent_adapter.RoomAdapterContract.Companion.ROOM_TABLE_NOTIFICATION_MESSAGE
import java.util.*

@Entity(tableName = ROOM_TABLE_NOTIFICATION_MESSAGE)
class RoomNotificationMessage: PersistentAdapterContract.PersistNotificationMessage {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = KEY_ACTION_ID)
    override var actionId: String = ""

    @ColumnInfo(name = KEY_TYPE)
    override var type: String = "notification"

    @ColumnInfo(name = KEY_PAYLOAD)
    override var payload: Map<String, String> = mapOf(KEY_ACTION_ID to actionId)

    @ColumnInfo(name = KEY_TIMESTAMP)
    override var timeStamp: Date = Date()

    @Ignore
    val notificationId = actionId.hashCode()
}