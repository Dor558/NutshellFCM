package com.dorbrauner.framework

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dorbrauner.framework.di.FirebaseMessagingComponents


internal class NotificationsRouterBroadcastReceiver: BroadcastReceiver() {

    private val notificationMessageReceiver: NotificationsFrameworkContract.NotificationsMessageReceiver =
            FirebaseMessagingComponents.notificationsMessageReceiver

    override fun onReceive(context: Context, intent: Intent) {
        notificationMessageReceiver.onNotificationsMessageReceived(intent)
    }
}