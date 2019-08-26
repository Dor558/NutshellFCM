package com.dorbrauner.framework

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.dorbrauner.framework.application.contexts.ApplicationContext
import com.dorbrauner.framework.extensions.TAG
import com.dorbrauner.framework.extensions.subscribeBy
import io.reactivex.schedulers.Schedulers


internal class NotificationsMessageReceiver(
    private val applicationContext: ApplicationContext,
    private val notificationsRepository: NotificationsFrameworkContract.Repository,
    private val androidNotificationBuilder: NotificationsFrameworkContract.AndroidNotificationBuilder
) : NotificationsFrameworkContract.NotificationsMessageReceiver {

    override fun onNotificationsMessageReceived(intent: Intent) {
        val actionId = intent.extras?.getString(NotificationsFrameworkContract.KEY_ACTION_ID)
            ?: throw NotificationsFrameworkContract.Error.UnknownNotificationIdThrowable(null)

        notificationsRepository
            .read(actionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { notificationMessage ->
                    when (notificationMessage.isSilent) {
                        true -> {
                            JobIntentService.enqueueWork(
                                applicationContext.get(),
                                SilentNotificationHandleService::class.java,
                                SILENT_NOTIFICATION_SERVICE_ID,
                                intent
                            )
                        }

                        false -> {
                            androidNotificationBuilder.build(notificationMessage)
                        }
                    }
                },
                onError = {
                    Log.e(TAG,"Failed to read notification message", it)
                }
            )
    }
}
