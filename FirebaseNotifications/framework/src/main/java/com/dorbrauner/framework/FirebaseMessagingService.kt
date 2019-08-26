package com.dorbrauner.framework

import android.util.Log
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.dorbrauner.framework.extensions.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.dorbrauner.framework.NotificationsFrameworkContract.Companion.KEY_ACTION_ID
import com.dorbrauner.framework.NotificationsFrameworkContract.Companion.KEY_IS_SILENT
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage
import org.joda.time.DateTime


open class FirebaseMessagingService : FirebaseMessagingService() {

    private val notificationsNotifier: NotificationsFrameworkContract.NotificationsNotifier = FirebaseMessagingComponents.notificationNotifier

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data
        val actionId = data[KEY_ACTION_ID]
        val isSilent = data[KEY_IS_SILENT]?.toBoolean() ?: false

        // Check if message contains a valid data payload.
        if (data.isEmpty() || actionId.isNullOrEmpty()) {
            return
        }

        notificationsNotifier.notifyMessage(NotificationMessage(actionId, DateTime(), isSilent, data))
    }

    override fun onNewToken(firebaseToken: String) {
        super.onNewToken(firebaseToken)
        Log.i(TAG, "FirebaseToken = $firebaseToken")
        FirebaseLoader.firebaseToken = firebaseToken
    }
}
