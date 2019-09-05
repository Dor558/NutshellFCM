package com.dorbrauner.nutshellfirebase

import com.dorbrauner.nutshellfirebase.model.NotificationMessage


internal class CleanupCase : NutshellFirebaseContract.NotificationsHandling.Case {

    override val actionIds: List<String> = emptyList()

    override fun consume(caseMessages: List<NotificationMessage>) {}
}