package com.dorbrauner.framework

import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationsHandling.Case
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage

internal class NotificationCasesManager(private val casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider):
    NotificationsFrameworkContract.NotificationsHandling.CasesManager {

    private var iterator: Iterator<Case>? = null

    override fun init() {
        iterator = casesProvider.cases.iterator()
    }

    override fun hasRemainingCases(): Boolean = iterator?.hasNext() == true

    override fun handleNextCase(notifications: List<NotificationMessage>): List<NotificationMessage> =
        iterator?.next()?.consume(notifications) ?: notifications
}