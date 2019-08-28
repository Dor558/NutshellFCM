package com.dorbrauner.framework

import com.dorbrauner.framework.database.model.NotificationMessage


internal class CleanupCase : NotificationsFrameworkContract.NotificationsHandling.Case {

    override val actionIds: List<String> = emptyList()

    override fun consume(caseMessages: List<NotificationMessage>) {}
}