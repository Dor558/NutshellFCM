package com.dorbrauner.room_persistent_adapter

import com.dorbrauner.persistentadapters.PersistentAdapterContract
import com.dorbrauner.persistentadapters.PersistentAdapterContract.*
import com.dorbrauner.room_persistent_adapter.model.RoomNotificationMessage


interface RoomAdapterContract {

    companion object {
        const val KEY_ACTION_ID = "action_id"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_PAYLOAD = "payload"
        const val KEY_TYPE = "type"
        const val ROOM_TABLE_NOTIFICATION_MESSAGE = "notification_messages"
    }

    interface Converter {
        fun convert(persistNotificationMessage: PersistNotificationMessage): RoomNotificationMessage
        fun convert(persistNotificationMessages: List<PersistNotificationMessage>): List<RoomNotificationMessage>
    }

}