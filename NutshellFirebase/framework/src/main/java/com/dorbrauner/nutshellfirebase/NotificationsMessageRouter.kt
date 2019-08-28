package com.dorbrauner.nutshellfirebase

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.dorbrauner.nutshellfirebase.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfirebase.extensions.TAG
import com.dorbrauner.nutshellfirebase.extensions.subscribeBy
import io.reactivex.schedulers.Schedulers


internal class NotificationsMessageRouter(
    private val applicationContext: ApplicationContext,
    private val notificationsRepository: NutshellFirebaseContract.Repository,
    private val androidNotificationBuilder: NutshellFirebaseContract.AndroidNotificationBuilder
) : NutshellFirebaseContract.NotificationsMessageRouter {

    override fun onRouteNotificationsMessage(intent: Intent) {
        val actionId = intent.extras?.getString(NutshellFirebaseContract.KEY_ACTION_ID)
            ?: throw NutshellFirebaseContract.Error.UnknownNotificationActionIdThrowable(null)

        notificationsRepository
            .read(actionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { notificationMessage ->
                    when (notificationMessage.type) {

                        NutshellFirebaseContract.NotificationType.NOTIFICATION -> {
                            androidNotificationBuilder.build(notificationMessage)
                        }

                        NutshellFirebaseContract.NotificationType.SILENT_NOTIFICATION -> {
                            JobIntentService.enqueueWork(
                                applicationContext.get(),
                                SilentNotificationHandleService::class.java,
                                SILENT_NOTIFICATION_SERVICE_ID,
                                intent
                            )
                        }

                        NutshellFirebaseContract.NotificationType.FOREGROUND_NOTIFICATION -> {
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
