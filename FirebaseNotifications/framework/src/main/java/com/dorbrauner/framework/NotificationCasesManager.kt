package com.dorbrauner.framework

import com.dorbrauner.framework.NotificationsFrameworkContract.NotificationsHandling.Case
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage

internal class NotificationCasesManager(private val casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider):
    NotificationsFrameworkContract.NotificationsHandling.CasesManager {

    private var iterator: Iterator<Case>? = null

    override fun init() {
        val cases = mutableListOf<Case>().apply {
            addAll(casesProvider.cases)
            add(CleanupCase())
        }

        iterator = cases.iterator()
    }

    override fun hasRemainingCases(): Boolean = iterator?.hasNext() == true

    override fun handleNextCase(notifications: List<NotificationMessage>): List<NotificationMessage> {
        val nextCase = iterator?.next()
        return nextCase?.let { case ->

            if (case is CleanupCase) {
                return notifications
            }

            val caseMessages = notifications.filter { case.actionIds.contains(it.actionId) }
            case.consume(caseMessages)
            caseMessages
        } ?: notifications
    }
}