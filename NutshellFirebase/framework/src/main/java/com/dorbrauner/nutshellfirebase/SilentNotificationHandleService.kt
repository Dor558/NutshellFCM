package com.dorbrauner.nutshellfirebase

import android.content.Intent
import androidx.core.app.JobIntentService
import com.dorbrauner.nutshellfirebase.di.FirebaseMessagingComponents

const val SILENT_NOTIFICATION_SERVICE_ID = 135

class SilentNotificationHandleService: JobIntentService() {

    private val notificationsConsumer: NutshellFirebaseContract.NotificationsConsumer = FirebaseMessagingComponents.notificationsConsumer

    override fun onHandleWork(intent: Intent) {
        val actionId = intent.extras?.getString(NutshellFirebaseContract.KEY_ACTION_ID)
            ?: throw NutshellFirebaseContract.Error.UnknownNotificationActionIdThrowable(null)
        notificationsConsumer.consume(actionId)
    }

}