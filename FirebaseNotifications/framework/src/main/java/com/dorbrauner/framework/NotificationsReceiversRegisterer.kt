package com.dorbrauner.framework

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationReceiversRegisterer.Companion.BACKGROUND_PRIORITY
import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationReceiversRegisterer.Companion.FOREGROUND_PRIORITY
import com.dorbrauner.framework.application.ActivityLifecycleCallback
import com.dorbrauner.framework.application.ApplicationLifeCycleWrapper
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.dorbrauner.framework.extensions.TAG
import com.dorbrauner.framework.extensions.subscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers

internal class NotificationsReceiversRegisterer(
    applicationLifeCycleWrapper: ApplicationLifeCycleWrapper,
    application: Application,
    private val notificationsConsumer: NotificationsFrameworkContract.NotificationsConsumer
): NotificationsFrameworkContract.NotificationReceiversRegisterer {

    private val notificationsRepository = FirebaseMessagingComponents.notificationsRepository

    private val applicationReceiver = NotificationsRouterBroadcastReceiver()

    private val activitiesReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val actionId = intent.extras?.getString(NotificationsFrameworkContract.KEY_ACTION_ID)
                ?: throw NotificationsFrameworkContract.Error.UnknownNotificationActionIdThrowable(null)

            notificationsRepository.read(actionId)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = { notificationMessage ->
                        when (notificationMessage.type) {
                            NotificationsFrameworkContract.NotificationType.NOTIFICATION -> {
                                notificationsConsumer.consumeNotificationsMessages()
                                abortBroadcast()
                            }
                            else -> {
                                //Ignore and forward broadcast to the messages router
                            }
                        }

                    },
                    onError = {
                        Log.e(TAG, "Failed to consume message via activity receiver")
                    }
                )
        }
    }

    private val activitiesLifeCycleCallback = object : ActivityLifecycleCallback {

        override fun onActivityStarted(activity: Activity) {
            val intentFilter = IntentFilter(NotificationsFrameworkContract.ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
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
        val intentFilter = IntentFilter(NotificationsFrameworkContract.ACTION_BROADCAST_REGISTRATION_NOTIFICATION)
        intentFilter.priority = BACKGROUND_PRIORITY
        application.registerReceiver(applicationReceiver, intentFilter)
        applicationLifeCycleWrapper.registerLifecycle(activitiesLifeCycleCallback)
    }

}