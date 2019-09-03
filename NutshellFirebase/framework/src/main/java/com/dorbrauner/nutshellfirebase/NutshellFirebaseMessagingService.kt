package com.dorbrauner.nutshellfirebase

import android.util.Log
import com.dorbrauner.nutshellfirebase.di.NutshellFirebaseComponents
import com.dorbrauner.nutshellfirebase.extensions.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_ACTION_ID
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_TYPE
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.NotificationType.Companion.NotificationType
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import java.util.*


open class NutshellFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationsNotifier: NutshellFirebaseContract.NotificationsNotifier = NutshellFirebaseComponents.notificationNotifier

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val actionId = data[KEY_ACTION_ID]
        val type = NotificationType(data[KEY_TYPE] ?: "")

        // Check if message contains a valid data payload.
        if (data.isEmpty() || actionId.isNullOrEmpty()) {
            return
        }

        notificationsNotifier.notifyMessage(
            NotificationMessage(
                actionId,
                type,
                data,
                Date()
            )
        )
    }

    override fun onNewToken(firebaseToken: String) {
        super.onNewToken(firebaseToken)
        Log.d(TAG, "FirebaseToken = $firebaseToken")
        NutshellFirebaseEngine.firebaseToken = firebaseToken
    }
}
