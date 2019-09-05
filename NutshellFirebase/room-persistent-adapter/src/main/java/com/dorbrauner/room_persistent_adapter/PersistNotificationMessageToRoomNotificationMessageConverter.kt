package com.dorbrauner.room_persistent_adapter

import com.dorbrauner.persistentadapters.PersistentAdapterContract.PersistNotificationMessage
import com.dorbrauner.room_persistent_adapter.model.RoomNotificationMessage

internal class PersistNotificationMessageToRoomNotificationMessageConverter : RoomAdapterContract.Converter {


    override fun convert(persistNotificationMessage: PersistNotificationMessage): RoomNotificationMessage {
        return RoomNotificationMessage().apply {
            actionId = persistNotificationMessage.actionId
            type = persistNotificationMessage.type
            timeStamp = persistNotificationMessage.timeStamp
            payload = persistNotificationMessage.payload
        }
    }

    override fun convert(persistNotificationMessages: List<PersistNotificationMessage>): List<RoomNotificationMessage> {
        return persistNotificationMessages.map { convert(it) }
    }

}