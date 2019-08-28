package com.dorbrauner.framework

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.dorbrauner.framework.NotificationsFrameworkContract.Companion.KEY_ACTION_ID

internal class NotificationDismissService: IntentService("NotificationDismissService") {

    private val TAG = this.javaClass.name

    override fun onHandleIntent(intent: Intent?) {
        intent?.getStringExtra(KEY_ACTION_ID)?.let { actionId ->
            Log.d(TAG, "Notification $actionId dismissed")
        }
    }
}