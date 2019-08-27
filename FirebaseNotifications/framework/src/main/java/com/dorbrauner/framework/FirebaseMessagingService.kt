package com.dorbrauner.framework

import android.util.Log
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.dorbrauner.framework.extensions.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.dorbrauner.framework.NotificationsFrameworkContract.Companion.KEY_ACTION_ID
import com.dorbrauner.framework.NotificationsFrameworkContract.Companion.KEY_TYPE
import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationType.Companion.NotificationType
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage
import org.joda.time.DateTime


open class FirebaseMessagingService : FirebaseMessagingService() {

    private val notificationsNotifier: NotificationsFrameworkContract.NotificationsNotifier = FirebaseMessagingComponents.notificationNotifier

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val data = remoteMessage.data
        val actionId = data[KEY_ACTION_ID]
        val type = NotificationType(data[KEY_TYPE] ?: "")

        // Check if message contains a valid data payload.
        if (data.isEmpty() || actionId.isNullOrEmpty()) {
            return
        }

        notificationsNotifier.notifyMessage(NotificationMessage(actionId, type, data, DateTime()))
    }

    override fun onNewToken(firebaseToken: String) {
        super.onNewToken(firebaseToken)
        Log.i(TAG, "FirebaseToken = $firebaseToken")
        FirebaseEngine.firebaseToken = firebaseToken
    }
}
