package com.dorbrauner.nutshellfirebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dorbrauner.nutshellfirebase.di.NutshellFirebaseComponents


internal class NotificationsRouterBroadcastReceiver: BroadcastReceiver() {

    private val notificationMessageRouter: NutshellFirebaseContract.NotificationsMessageRouter =
            NutshellFirebaseComponents.notificationsMessageRouter

    override fun onReceive(context: Context, intent: Intent) {
        notificationMessageRouter.onRouteNotificationsMessage(intent)
    }
}