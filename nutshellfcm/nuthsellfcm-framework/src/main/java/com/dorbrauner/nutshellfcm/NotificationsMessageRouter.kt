package com.dorbrauner.nutshellfcm

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.dorbrauner.nutshellfcm.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfcm.extensions.TAG
import com.dorbrauner.rxworkframework.scheudlers.Schedulers

internal class NotificationsMessageRouter(
    private val applicationContext: ApplicationContext,
    private val notificationsRepository: NutshellFCMContract.Repository,
    private val androidNotificationBuilder: NutshellFCMContract.AndroidNotificationBuilder
) : NutshellFCMContract.NotificationsMessageRouter {

    override fun onRouteNotificationsMessage(intent: Intent) {
        val actionId = intent.extras?.getString(NutshellFCMContract.KEY_ACTION_ID)
            ?: throw NutshellFCMContract.Error.UnknownNotificationActionIdThrowable(null)

        notificationsRepository
            .read(actionId)
            .subscribeOn(Schedulers.bounded)
            .subscribe(
                onResult = { notificationMessage ->
                    when (notificationMessage.type) {

                        NutshellFCMContract.NotificationType.NOTIFICATION -> {
                            androidNotificationBuilder.build(notificationMessage)
                        }

                        NutshellFCMContract.NotificationType.SILENT_NOTIFICATION -> {
                            JobIntentService.enqueueWork(
                                applicationContext.get(),
                                SilentNotificationHandleService::class.java,
                                SILENT_NOTIFICATION_SERVICE_ID,
                                intent
                            )
                        }

                        NutshellFCMContract.NotificationType.FOREGROUND_NOTIFICATION -> {
                            androidNotificationBuilder.buildForeground(notificationMessage)
                        }
                    }
                },

                onError = {
                    Log.e(TAG, "Failed to read notification message", it)
                }
            )
    }
}
