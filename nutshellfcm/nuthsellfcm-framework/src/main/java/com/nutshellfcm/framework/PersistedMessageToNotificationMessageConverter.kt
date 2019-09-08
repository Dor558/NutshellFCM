package com.nutshellfcm.framework

import com.nutshellfcm.framework.NutshellFCMContract.NotificationType.Companion.NotificationType
import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.common.PersistentAdapterContract.*
import java.util.*


internal class PersistedMessageToNotificationMessageConverter
    : NutshellFCMContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter
{

    override fun convert(persistNotificationMessage: PersistNotificationMessage): NotificationMessage {
        return NotificationMessage(
            actionId = persistNotificationMessage.actionId,
            type = NotificationType(persistNotificationMessage.type),
            timeStamp = persistNotificationMessage.timeStamp,
            payload = persistNotificationMessage.payload
        )
    }

    override fun convert(persistNotificationMessages: List<PersistNotificationMessage>): List<NotificationMessage> {
        return persistNotificationMessages.map { convert(it) }
    }

    override fun convertBack(notificationMessage: NotificationMessage): PersistNotificationMessage {
        return object : PersistNotificationMessage {
            override val actionId: String = notificationMessage.actionId
            override val type: String = notificationMessage.type.value
            override val payload: Map<String, String> = notificationMessage.payload
            override val timeStamp: Date = notificationMessage.timeStamp
        }
    }

    override fun convertBack(notificationMessages: List<NotificationMessage>): List<PersistNotificationMessage> {
        return notificationMessages.map { convertBack(it) }
    }
}