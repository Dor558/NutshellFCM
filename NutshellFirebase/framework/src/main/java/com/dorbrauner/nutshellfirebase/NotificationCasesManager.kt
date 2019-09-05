package com.dorbrauner.nutshellfirebase

import androidx.lifecycle.MutableLiveData
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.NotificationsHandling.Case
import com.dorbrauner.nutshellfirebase.model.NotificationMessage

internal class NotificationCasesManager(private val casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider,
                                        private val handledNotificationsNotifier: NutshellFirebaseContract.NotificationsHandling.HandledNotificationsNotifier):
    NutshellFirebaseContract.NotificationsHandling.CasesManager {

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
                return notifications //Consume all unhandled notifications
            }

            val caseMessages = notifications.filter { case.actionIds.contains(it.actionId) }
            if (caseMessages.isNotEmpty()) {
                case.consume(caseMessages)
                (handledNotificationsNotifier.handledNotifications as MutableLiveData).value = caseMessages
            }

            caseMessages
        } ?: notifications
    }
}