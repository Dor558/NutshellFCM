package com.dorbrauner.framework

import com.dorbrauner.framework.application.contexts.ApplicationContext
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.*


internal class AndroidNotificationBuilder(private val applicationContext: ApplicationContext,
                                 private val notificationManager: NotificationsFrameworkContract.AndroidNotificationsManager,
                                 private val notificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory
)
    : NotificationsFrameworkContract.AndroidNotificationBuilder {


    override fun build(notificationMessage: NotificationMessage) {
        val androidNotification = notificationsFactory.create(notificationMessage)
        notificationManager.show(applicationContext.get(), androidNotification)
    }
}