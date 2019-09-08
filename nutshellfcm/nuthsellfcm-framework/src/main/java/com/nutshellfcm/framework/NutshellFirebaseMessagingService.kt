package com.nutshellfcm.framework

import android.util.Log
import com.nutshellfcm.framework.di.NutshellFirebaseComponents
import com.nutshellfcm.framework.extensions.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nutshellfcm.framework.NutshellFCMContract.Companion.KEY_ACTION_ID
import com.nutshellfcm.framework.NutshellFCMContract.Companion.KEY_TYPE
import com.nutshellfcm.framework.NutshellFCMContract.NotificationType.Companion.NotificationType
import com.nutshellfcm.framework.model.NotificationMessage
import java.util.*


open class NutshellFirebaseMessagingService : FirebaseMessagingService() {

    private val notificationsNotifier: NutshellFCMContract.NotificationsNotifier = NutshellFirebaseComponents.notificationNotifier

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
        NutshellFCMEngine.firebaseToken = firebaseToken
    }
}
