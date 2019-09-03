package com.dorbrauner.nutshellfirebase.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.*
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_ACTION_ID
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_PAYLOAD
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_TIMESTAMP
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_TYPE
import java.util.*

@Entity(tableName = Sources.PersistentSource.ROOM_TABLE_NOTIFICATION_MESSAGE)
data class NotificationMessage(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = KEY_ACTION_ID)
    val actionId: String,

    @ColumnInfo(name = KEY_TYPE)
    val type: NotificationType = NotificationType.NOTIFICATION,

    @ColumnInfo(name = KEY_PAYLOAD)
    val payload: Map<String, String> = mapOf(KEY_ACTION_ID to actionId),

    @ColumnInfo(name = KEY_TIMESTAMP)
    val timeStamp: Date = Date()
) {
    @Ignore
    val notificationId = actionId.hashCode()
}