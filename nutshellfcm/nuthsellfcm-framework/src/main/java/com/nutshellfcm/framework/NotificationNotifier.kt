package com.nutshellfcm.framework

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_RECEIVER_FOREGROUND
import android.util.Log
import com.nutshellfcm.framework.NutshellFCMContract.Companion.ACTION_BROADCAST_REGISTRATION_NOTIFICATION
import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.framework.extensions.TAG
import com.nutshellfcm.framework.extensions.toBundle
import com.nutshellfcm.rxworkframework.scheudlers.Schedulers

class NotificationNotifier(
    private val application: Application,
    private val notificationMessageWriter: NutshellFCMContract.NotificationMessageWriter
) : NutshellFCMContract.NotificationsNotifier {

    override fun notifyMessage(notificationMessage: NotificationMessage) {
        notificationMessageWriter.write(notificationMessage)
            .subscribeOn(Schedulers.single)
            .subscribe(
                onResult = {
                    sendBroadcast(notificationMessage)
                },
                onError = {
                    Log.e(TAG, "failed to write android notification", it)
                }
            )
    }

    private fun sendBroadcast(notificationMessage: NotificationMessage) {
        val broadcast = Intent(ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
        broadcast.flags = FLAG_RECEIVER_FOREGROUND;
        broadcast.putExtras(notificationMessage.payload.toBundle())
        application.sendOrderedBroadcast(
            broadcast,
            null,
            null,
            NutshellHandler,
            Activity.RESULT_OK,
            null,
            broadcast.extras
        )
    }

}