package com.dorbrauner.nutshellfirebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dorbrauner.nutshellfirebase.di.FirebaseMessagingComponents


internal class NotificationsRouterBroadcastReceiver: BroadcastReceiver() {

    private val notificationMessageRouter: NutshellFirebaseContract.NotificationsMessageRouter =
            FirebaseMessagingComponents.notificationsMessageRouter

    override fun onReceive(context: Context, intent: Intent) {
        notificationMessageRouter.onRouteNotificationsMessage(intent)
    }
}