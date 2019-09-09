package com.nutshellfcm.framework

import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.framework.di.NutshellFirebaseComponents


object NutshellLocalMessagesNotifier {

    fun notify(notificationMessage: NotificationMessage) {
        NutshellFirebaseComponents.notificationNotifier.notifyMessage(notificationMessage)
    }

    fun notifyDismiss(actionId: String) {
        NutshellFirebaseComponents.notificationsConsumer.consume(actionId)
    }

}