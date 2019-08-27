package com.dorbrauner.framework

import android.content.Intent
import androidx.core.os.bundleOf
import com.dorbrauner.framework.NotificationsFrameworkContract.Error.UnknownServiceBindActionIdThrowable
import com.dorbrauner.framework.application.contexts.ApplicationContext
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.*


internal class AndroidNotificationBuilder(
    private val applicationContext: ApplicationContext,
    private val foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder,
    private val notificationManager: NotificationsFrameworkContract.AndroidNotificationsManager,
    private val notificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory
) : NotificationsFrameworkContract.AndroidNotificationBuilder {


    override fun build(notificationMessage: NotificationMessage) {
        val androidNotification = notificationsFactory.create(notificationMessage)
        notificationManager.show(applicationContext.get(), androidNotification)
    }

    override fun buildForeground(notificationMessage: NotificationMessage) {
        val intent = Intent(
            applicationContext.get(),
            foregroundServicesBinder.bind(notificationMessage.actionId) ?: throw UnknownServiceBindActionIdThrowable(notificationMessage.actionId)
        )
        val extras = bundleOf(NotificationsFrameworkContract.KEY_ACTION_ID to notificationMessage.actionId)
        intent.putExtras(extras)
        applicationContext.get().startService(intent)
    }
}