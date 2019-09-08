package com.dorbrauner.nutshellfcm

import com.dorbrauner.nutshellfcm.model.NotificationMessage


internal class CleanupCase : NutshellFCMContract.NotificationsHandling.Case {

    override val actionIds: List<String> = emptyList()

    override fun consume(caseMessages: List<NotificationMessage>) {}
}