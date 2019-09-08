package com.dorbrauner.nutshellfirebase

import android.content.Intent
import androidx.core.os.bundleOf
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Error.UnknownServiceBindActionIdThrowable
import com.dorbrauner.nutshellfirebase.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfirebase.model.NotificationMessage


internal class AndroidNotificationBuilder(
    private val applicationContext: ApplicationContext,
    private val foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder,
    private val notificationManager: NutshellFirebaseContract.AndroidNotificationsManager,
    private val notificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory
) : NutshellFirebaseContract.AndroidNotificationBuilder {


    override fun build(notificationMessage: NotificationMessage) {
        val androidNotification = notificationsFactory.create(notificationMessage)
        notificationManager.show(applicationContext.get(), androidNotification)
    }

    override fun buildForeground(notificationMessage: NotificationMessage) {
        val intent = Intent(
            applicationContext.get(),
            foregroundServicesBinder.bind(notificationMessage.actionId) ?: throw UnknownServiceBindActionIdThrowable(notificationMessage.actionId)
        )
        val extras = bundleOf(NutshellFirebaseContract.KEY_ACTION_ID to notificationMessage.actionId)
        intent.putExtras(extras)
        applicationContext.get().startService(intent)
    }
}