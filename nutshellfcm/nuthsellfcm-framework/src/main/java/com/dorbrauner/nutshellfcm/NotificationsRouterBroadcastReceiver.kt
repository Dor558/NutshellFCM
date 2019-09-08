package com.dorbrauner.nutshellfcm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dorbrauner.nutshellfcm.di.NutshellFirebaseComponents


internal class NotificationsRouterBroadcastReceiver: BroadcastReceiver() {

    private val notificationMessageRouter: NutshellFCMContract.NotificationsMessageRouter =
            NutshellFirebaseComponents.notificationsMessageRouter

    override fun onReceive(context: Context, intent: Intent) {
        notificationMessageRouter.onRouteNotificationsMessage(intent)
    }
}