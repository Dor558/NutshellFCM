package com.dorbrauner.framework

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationReceiversRegisterer.Companion.BACKGROUND_PRIORITY
import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationReceiversRegisterer.Companion.FOREGROUND_PRIORITY
import com.dorbrauner.framework.application.ActivityLifecycleCallback
import com.dorbrauner.framework.application.ApplicationLifeCycleWrapper

internal class NotificationsReceiversRegisterer(
    applicationLifeCycleWrapper: ApplicationLifeCycleWrapper,
    application: Application,
    private val notificationsConsumer: NotificationsFrameworkContract.NotificationsConsumer
): NotificationsFrameworkContract.NotificationReceiversRegisterer {


    private val applicationReceiver = NotificationsRouterBroadcastReceiver()

    private val activitiesReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            notificationsConsumer.consumeAll()
            abortBroadcast()
        }
    }

    private val activitiesLifeCycleCallback = object : ActivityLifecycleCallback {

        override fun onActivityStarted(activity: Activity) {
            val intentFilter = IntentFilter(NotificationsFrameworkContract.ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
            intentFilter.priority = FOREGROUND_PRIORITY
            activity.registerReceiver(activitiesReceiver, intentFilter)
        }

        override fun onActivityResumed(activity: Activity) {
            notificationsConsumer.consumeAll()
        }

        override fun onActivityStopped(activity: Activity) {
            activity.unregisterReceiver(activitiesReceiver)
        }
    }

    init {
        val intentFilter = IntentFilter(NotificationsFrameworkContract.ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
        intentFilter.priority = BACKGROUND_PRIORITY
        application.registerReceiver(applicationReceiver, intentFilter)
        applicationLifeCycleWrapper.registerLifecycle(activitiesLifeCycleCallback)
    }

}