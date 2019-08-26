package com.dorbrauner.framework

import android.app.NotificationManager
import android.util.Log
import com.dorbrauner.framework.extensions.TAG
import com.dorbrauner.framework.extensions.subscribeBy
import io.reactivex.android.schedulers.AndroidSchedulers


internal class NotificationsConsumer(
    private val notificationManager: NotificationManager,
    private val notificationsRepository: NotificationsFrameworkContract.Repository,
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

    override fun consumeAll() {
        notificationsRepository.read()
            .doOnSubscribe {
                casesManager.init()
            }
            .map { notificationMessages ->
                consumeRecursive(notificationMessages)
            }.flatMapCompletable {
                notificationsRepository.purge()
            }
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
            notificationsRepository.remove(notificationMessage.actionId)
            notificationManager.cancel(notificationMessage.notificationId)
        }

        consumeRecursive(notificationMessages - consumedNotifications)
    }
}