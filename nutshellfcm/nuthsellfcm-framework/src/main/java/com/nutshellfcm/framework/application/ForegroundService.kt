package com.nutshellfcm.framework.application

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.di.NutshellFirebaseComponents
import com.nutshellfcm.framework.extensions.TAG
import com.nutshellfcm.framework.extensions.toBundle
import com.nutshellfcm.rxworkframework.scheudlers.Schedulers


abstract class ForegroundService : Service() {

    private val androidNotificationsFactory = NutshellFirebaseComponents.androidNotificationsFactory

    private val notificationsRepository = NutshellFirebaseComponents.notificationsRepository

    private val androidNotificationsManager = NutshellFirebaseComponents.notificationsManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val actionId = intent?.extras?.getString(NutshellFCMContract.KEY_ACTION_ID)
            ?: throw NutshellFCMContract.Error.UnknownNotificationActionIdThrowable(null)

        notificationsRepository.read(actionId)
            .subscribeOn(Schedulers.unbounded)
            .subscribe(
                onResult = { notificationMessage ->
                    val androidNotification = androidNotificationsFactory.create(notificationMessage)
                    androidNotificationsManager.showForeground(this, notificationMessage.notificationId, androidNotification)
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