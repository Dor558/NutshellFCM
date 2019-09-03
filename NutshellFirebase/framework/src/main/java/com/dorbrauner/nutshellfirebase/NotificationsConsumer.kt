package com.dorbrauner.nutshellfirebase

import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.*
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Error.*
import com.dorbrauner.nutshellfirebase.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.nutshellfirebase.extensions.TAG
import com.dorbrauner.rxworkframework.scheudlers.Schedulers

internal class NotificationsConsumer(
    private val applicationContext: ApplicationContext,
    private val systemNotificationManager: NotificationManager,
    private val notificationsRepository: Repository,
    private val foregroundServicesBinder: ForegroundServicesBinder,
    private val casesManager: NotificationsHandling.CasesManager
) : NutshellFirebaseContract.NotificationsConsumer {


    override fun consume(actionId: String) {
        notificationsRepository.read(actionId)
            .doOnSubscribe {
                casesManager.init()
            }.map {
                consumeRecursive(listOf(it))
            }.flatMap {
                notificationsRepository.remove(actionId)
            }
            .subscribeOn(Schedulers.unbounded)
            .observeOn(Schedulers.main)
            .subscribe(
                onResult = {
                    Log.d(TAG, "notification of id $actionId consumed")
                },
                onError = {
                    Log.e(TAG, "error consuming notification of $actionId", it)
                }
            )
    }

    override fun consumeNotificationsMessages() {
        notificationsRepository.read()
            .doOnSubscribe {
                casesManager.init()
            }
            .map { notificationMessages ->
                notificationMessages.filter { it.type == NotificationType.NOTIFICATION }
            }
            .map { notificationMessages ->
                consumeRecursive(notificationMessages)
                notificationMessages.map { it.actionId }
            }.map { notificationMessages ->
                notificationsRepository.remove(notificationMessages)
            }
            .subscribeOn(Schedulers.unbounded)
            .observeOn(Schedulers.main)
            .subscribe(
                onResult = {
                    Log.d(TAG, "all notifications consumed")
                },
                onError = {
                    Log.e(TAG, "error consuming all notifications", it)
                }
            )
    }

    private fun consumeRecursive(notificationMessages: List<NotificationMessage>) {
        if (!casesManager.hasRemainingCases() || notificationMessages.isEmpty()) {
            return
        }

        val consumedNotifications = casesManager.handleNextCase(notificationMessages)
        consumedNotifications.forEach { notificationMessage ->
            when (notificationMessage.type) {
                NotificationType.FOREGROUND_NOTIFICATION -> {
                    applicationContext.get().stopService(Intent(
                        applicationContext.get(),
                        foregroundServicesBinder.bind(notificationMessage.actionId)
                            ?: throw UnknownServiceBindActionIdThrowable(notificationMessage.actionId)
                    ))
                }

                else -> {
                    systemNotificationManager.cancel(notificationMessage.notificationId)
                }
            }
        }

        consumeRecursive(notificationMessages - consumedNotifications)
    }
}