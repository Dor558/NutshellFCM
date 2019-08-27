package com.dorbrauner.framework

import com.dorbrauner.framework.di.FirebaseMessagingComponents


object LocalMessagesNotifier {

    fun notify(notificationMessage: NotificationsFrameworkContract.Repository.NotificationMessage) {
        FirebaseMessagingComponents.notificationNotifier.notifyMessage(notificationMessage)
    }

    fun notifyDismiss(actionId: String) {
        FirebaseMessagingComponents.notificationsConsumer.consume(actionId)
    }

}