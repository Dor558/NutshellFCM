package com.dorbrauner.nutshellfcm

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.dorbrauner.nutshellfcm.NutshellFCMContract.*
import com.dorbrauner.nutshellfcm.NutshellFCMContract.NotificationReceiversRegisterer.Companion.BACKGROUND_PRIORITY
import com.dorbrauner.nutshellfcm.NutshellFCMContract.NotificationReceiversRegisterer.Companion.FOREGROUND_PRIORITY
import com.dorbrauner.nutshellfcm.application.ActivityLifecycleCallback
import com.dorbrauner.nutshellfcm.application.ApplicationLifeCycleWrapper
import com.dorbrauner.nutshellfcm.di.NutshellFirebaseComponents
import com.dorbrauner.nutshellfcm.extensions.TAG
import com.dorbrauner.rxworkframework.scheudlers.Schedulers

internal class NotificationsReceiversRegisterer(
    applicationLifeCycleWrapper: ApplicationLifeCycleWrapper,
    application: Application,
    private val notificationsConsumer: NutshellFCMContract.NotificationsConsumer
): NotificationReceiversRegisterer {

    private val notificationsRepository = NutshellFirebaseComponents.notificationsRepository

    private val applicationReceiver = NotificationsRouterBroadcastReceiver()

    private val activitiesReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val actionId = intent.extras?.getString(NutshellFCMContract.KEY_ACTION_ID)
                ?: throw Error.UnknownNotificationActionIdThrowable(null)

            runCatching {
                val notificationMessage = notificationsRepository.read(actionId)
                    .subscribeOn(Schedulers.single)
                    .blockingGet()

                Log.i(TAG, "Recevied notification message $notificationMessage")
                when (notificationMessage?.type) {
                    NotificationType.NOTIFICATION -> {
                        notificationsConsumer.consumeNotificationsMessages()
                        abortBroadcast()
                    }
                    else -> {
                        //Ignore and forward broadcast to the messages router
                    }
                }
            }.getOrElse {
                Log.e(TAG, "failed to consume notification with action id $actionId")
            }
        }
    }

    private val activitiesLifeCycleCallback = object : ActivityLifecycleCallback {

        override fun onActivityStarted(activity: Activity) {
            val intentFilter = IntentFilter(NutshellFCMContract.ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
            intentFilter.priority = FOREGROUND_PRIORITY
            activity.registerReceiver(activitiesReceiver, intentFilter)
        }

        override fun onActivityResumed(activity: Activity) {
            notificationsConsumer.consumeNotificationsMessages()
        }

        override fun onActivityStopped(activity: Activity) {
            activity.unregisterReceiver(activitiesReceiver)
        }
    }

    init {
        val intentFilter = IntentFilter(NutshellFCMContract.ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
        intentFilter.priority = BACKGROUND_PRIORITY
        application.registerReceiver(applicationReceiver, intentFilter)
        applicationLifeCycleWrapper.registerLifecycle(activitiesLifeCycleCallback)
    }

}