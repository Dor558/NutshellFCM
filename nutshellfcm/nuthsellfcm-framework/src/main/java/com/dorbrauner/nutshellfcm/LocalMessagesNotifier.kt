package com.dorbrauner.nutshellfcm

import com.dorbrauner.nutshellfcm.model.NotificationMessage
import com.dorbrauner.nutshellfcm.di.NutshellFirebaseComponents


object LocalMessagesNotifier {

    fun notify(notificationMessage: NotificationMessage) {
        NutshellFirebaseComponents.notificationNotifier.notifyMessage(notificationMessage)
    }

    fun notifyDismiss(actionId: String) {
        NutshellFirebaseComponents.notificationsConsumer.consume(actionId)
    }

}