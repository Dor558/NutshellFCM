package com.nutshellfcm.framework

import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import com.nutshellfcm.framework.NutshellFCMContract.*
import com.nutshellfcm.framework.NutshellFCMContract.Error.*
import com.nutshellfcm.framework.application.contexts.ApplicationContext
import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.framework.extensions.TAG
import com.nutshellfcm.rxworkframework.scheudlers.Schedulers

internal class NotificationsConsumer(
    private val applicationContext: ApplicationContext,
    private val systemNotificationManager: NotificationManager,
    private val notificationsRepository: Repository,
    private val foregroundServicesBinder: ForegroundServicesBinder,
    private val casesManager: NotificationsHandling.CasesManager
) : NutshellFCMContract.NotificationsConsumer {


    override fun consume(actionId: String) {
        notificationsRepository.read(actionId)
            .doOnSubscribe {
                casesManager.init()
            }
            .observeOn(Schedulers.main)
            .map {
                consumeRecursive(listOf(it))
            }
            .observeOn(Schedulers.unbounded)
            .flatMap {
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
            .observeOn(Schedulers.main)
            .map { notificationMessages ->
                consumeRecursive(notificationMessages)
                notificationMessages.map { it.actionId }
            }
            .observeOn(Schedulers.unbounded)
            .flatMap { notificationMessages ->
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