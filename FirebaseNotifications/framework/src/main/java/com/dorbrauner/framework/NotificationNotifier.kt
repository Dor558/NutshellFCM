package com.dorbrauner.framework

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dorbrauner.framework.extensions.TAG
import com.dorbrauner.framework.extensions.subscribeBy
import com.dorbrauner.framework.extensions.toBundle
import com.dorbrauner.framework.NotificationsFrameworkContract.Companion.ACTION_BROADCAST_REGISTRATION_NOTIFICATION
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class NotificationNotifier(
    private val application: Application,
    private val notificationMessageWriter: NotificationsFrameworkContract.NotificationMessageWriter
) : NotificationsFrameworkContract.NotificationsNotifier {

    override fun notifyMessage(notificationMessage: NotificationMessage) {
        notificationMessageWriter.write(notificationMessage)
            .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
            .subscribeBy(
                onComplete = {
                    sendBroadcast(notificationMessage)
                },
                onError = {
                    Log.e(TAG, "failed to write android notification", it)
                }
            )
    }

    private fun sendBroadcast(notificationMessage: NotificationMessage) {
        val broadcast = Intent(ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
        broadcast.putExtras(notificationMessage.payload.toBundle())
        application.sendOrderedBroadcast(
            broadcast,
            null,
            null,
            null,
            Activity.RESULT_OK,
            null,
            broadcast.extras
        )
    }

}