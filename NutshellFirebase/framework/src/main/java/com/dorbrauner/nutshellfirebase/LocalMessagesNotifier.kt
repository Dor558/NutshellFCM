package com.dorbrauner.nutshellfirebase

import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.nutshellfirebase.di.FirebaseMessagingComponents


object LocalMessagesNotifier {

    fun notify(notificationMessage: NotificationMessage) {
        FirebaseMessagingComponents.notificationNotifier.notifyMessage(notificationMessage)
    }

    fun notifyDismiss(actionId: String) {
        FirebaseMessagingComponents.notificationsConsumer.consume(actionId)
    }

}