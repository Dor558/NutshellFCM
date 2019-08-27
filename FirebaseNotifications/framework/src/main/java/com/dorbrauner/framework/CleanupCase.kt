package com.dorbrauner.framework

import com.dorbrauner.framework.NotificationsFrameworkContract.Repository


internal class CleanupCase : NotificationsFrameworkContract.NotificationsHandling.Case {

    override val actionIds: List<String> = emptyList()

    override fun consume(caseMessages: List<Repository.NotificationMessage>) {}
}