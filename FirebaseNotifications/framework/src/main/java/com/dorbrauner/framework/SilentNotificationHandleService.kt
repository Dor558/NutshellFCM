package com.dorbrauner.framework

import android.content.Intent
import androidx.core.app.JobIntentService
import com.dorbrauner.framework.di.FirebaseMessagingComponents

const val SILENT_NOTIFICATION_SERVICE_ID = 135

class SilentNotificationHandleService: JobIntentService() {

    private val notificationsConsumer: NotificationsFrameworkContract.NotificationsConsumer = FirebaseMessagingComponents.notificationsConsumer

    override fun onHandleWork(intent: Intent) {
        val actionId = intent.extras?.getString(NotificationsFrameworkContract.KEY_ACTION_ID)
            ?: throw NotificationsFrameworkContract.Error.UnknownNotificationIdThrowable(null)
        notificationsConsumer.consume(actionId)
    }

}