package com.dorbrauner.framework.application

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.dorbrauner.framework.extensions.TAG
import com.dorbrauner.framework.extensions.subscribeBy
import com.dorbrauner.framework.extensions.toBundle
import io.reactivex.schedulers.Schedulers


abstract class ForegroundService : Service() {

    private val androidNotificationsFactory = FirebaseMessagingComponents.androidNotificationsFactory

    private val notificationsRepository = FirebaseMessagingComponents.notificationsRepository

    private val androidNotificationsManager = FirebaseMessagingComponents.notificationsManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val actionId = intent?.extras?.getString(NotificationsFrameworkContract.KEY_ACTION_ID)
            ?: throw NotificationsFrameworkContract.Error.UnknownNotificationActionIdThrowable(null)

        notificationsRepository.read(actionId)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { notificationMessage ->
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