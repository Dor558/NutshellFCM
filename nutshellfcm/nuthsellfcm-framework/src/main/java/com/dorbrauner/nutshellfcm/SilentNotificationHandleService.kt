package com.dorbrauner.nutshellfcm

import android.content.Intent
import androidx.core.app.JobIntentService
import com.dorbrauner.nutshellfcm.di.NutshellFirebaseComponents

const val SILENT_NOTIFICATION_SERVICE_ID = 135

class SilentNotificationHandleService: JobIntentService() {

    private val notificationsConsumer: NutshellFCMContract.NotificationsConsumer = NutshellFirebaseComponents.notificationsConsumer

    override fun onHandleWork(intent: Intent) {
        val actionId = intent.extras?.getString(NutshellFCMContract.KEY_ACTION_ID)
            ?: throw NutshellFCMContract.Error.UnknownNotificationActionIdThrowable(null)
        notificationsConsumer.consume(actionId)
    }

}