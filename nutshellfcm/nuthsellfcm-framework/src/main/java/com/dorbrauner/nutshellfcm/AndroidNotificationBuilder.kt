package com.dorbrauner.nutshellfcm

import android.content.Intent
import androidx.core.os.bundleOf
import com.dorbrauner.nutshellfcm.NutshellFCMContract.Error.UnknownServiceBindActionIdThrowable
import com.dorbrauner.nutshellfcm.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfcm.model.NotificationMessage


internal class AndroidNotificationBuilder(
    private val applicationContext: ApplicationContext,
    private val foregroundServicesBinder: NutshellFCMContract.ForegroundServicesBinder,
    private val notificationManager: NutshellFCMContract.AndroidNotificationsManager,
    private val notificationsFactory: NutshellFCMContract.AndroidNotificationsFactory
) : NutshellFCMContract.AndroidNotificationBuilder {


    override fun build(notificationMessage: NotificationMessage) {
        val androidNotification = notificationsFactory.create(notificationMessage)
        notificationManager.show(applicationContext.get(), androidNotification)
    }

    override fun buildForeground(notificationMessage: NotificationMessage) {
        val intent = Intent(
            applicationContext.get(),
            foregroundServicesBinder.bind(notificationMessage.actionId) ?: throw UnknownServiceBindActionIdThrowable(notificationMessage.actionId)
        )
        val extras = bundleOf(NutshellFCMContract.KEY_ACTION_ID to notificationMessage.actionId)
        intent.putExtras(extras)
        applicationContext.get().startService(intent)
    }
}