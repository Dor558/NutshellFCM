package com.dorbrauner.nutshellfirebase

import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.nutshellfirebase.di.NutshellFirebaseComponents


object LocalMessagesNotifier {

    fun notify(notificationMessage: NotificationMessage) {
        NutshellFirebaseComponents.notificationNotifier.notifyMessage(notificationMessage)
    }

    fun notifyDismiss(actionId: String) {
        NutshellFirebaseComponents.notificationsConsumer.consume(actionId)
    }

}