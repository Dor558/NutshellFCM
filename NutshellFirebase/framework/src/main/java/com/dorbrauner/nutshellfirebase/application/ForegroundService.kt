package com.dorbrauner.nutshellfirebase.application

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.di.FirebaseMessagingComponents
import com.dorbrauner.nutshellfirebase.extensions.TAG
import com.dorbrauner.nutshellfirebase.extensions.toBundle
import com.dorbrauner.rxworkframework.scheudlers.Schedulers


abstract class ForegroundService : Service() {

    private val androidNotificationsFactory = FirebaseMessagingComponents.androidNotificationsFactory

    private val notificationsRepository = FirebaseMessagingComponents.notificationsRepository

    private val androidNotificationsManager = FirebaseMessagingComponents.notificationsManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val actionId = intent?.extras?.getString(NutshellFirebaseContract.KEY_ACTION_ID)
            ?: throw NutshellFirebaseContract.Error.UnknownNotificationActionIdThrowable(null)

        notificationsRepository.read(actionId)
            .subscribeOn(Schedulers.unbounded)
            .subscribe(
                onResult = { notificationMessage ->
                    val androidNotification = androidNotificationsFactory.create(notificationMessage)
                    androidNotificationsManager.showForeground(this, androidNotification)
                    onForegroundStarted(notificationMessage.payload.toBundle())
                },

                onError = {
                    Log.e(TAG, "failed to start foreground service  $TAG ")
                }
            )

        return super.onStartCommand(intent, flags, startId)
    }

    abstract fun onForegroundStarted(payload: Bundle)

}