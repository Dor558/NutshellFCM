package com.dorbrauner.framework

import android.app.NotificationManager
import android.content.Intent
import android.util.Log
import com.dorbrauner.framework.NotificationsFrameworkContract.Error.*
import com.dorbrauner.framework.application.contexts.ApplicationContext
import com.dorbrauner.framework.extensions.TAG
import com.dorbrauner.framework.extensions.subscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers


internal class NotificationsConsumer(
    private val applicationContext: ApplicationContext,
    private val systemNotificationManager: NotificationManager,
    private val notificationsRepository: NotificationsFrameworkContract.Repository,
    private val foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder,
    private val casesManager: NotificationsFrameworkContract.NotificationsHandling.CasesManager
) : NotificationsFrameworkContract.NotificationsConsumer {


    override fun consume(actionId: String) {
        notificationsRepository.read(actionId)
            .doOnSubscribe {
                casesManager.init()
            }.map {
                consumeRecursive(listOf(it))
            }.flatMapCompletable {
                notificationsRepository.remove(actionId)
            }.subscribeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    Log.i(TAG, "notification of id $actionId consumed")
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
                notificationMessages.filter { it.type == NotificationsFrameworkContract.NotificationType.NOTIFICATION }
            }
            .map { notificationMessages ->
                consumeRecursive(notificationMessages)
                notificationMessages
            }.map { notificationMessages ->
                notificationMessages.forEach { notificationsRepository.remove(it.actionId) }
            }
            .ignoreElement()
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    Log.i(TAG, "all notifications consumed")
                },
                onError = {
                    Log.e(TAG, "error consuming all notifications", it)
                }
            )
    }

    private fun consumeRecursive(notificationMessages: List<NotificationsFrameworkContract.Repository.NotificationMessage>) {
        if (!casesManager.hasRemainingCases() || notificationMessages.isEmpty()) {
            return
        }

        val consumedNotifications = casesManager.handleNextCase(notificationMessages)
        consumedNotifications.forEach { notificationMessage ->
            when (notificationMessage.type) {
                NotificationsFrameworkContract.NotificationType.FOREGROUND_NOTIFICATION -> {
                    applicationContext.get().stopService(Intent(
                        applicationContext.get(),
                        foregroundServicesBinder.bind(notificationMessage.actionId) ?: throw UnknownServiceBindActionIdThrowable(notificationMessage.actionId)
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