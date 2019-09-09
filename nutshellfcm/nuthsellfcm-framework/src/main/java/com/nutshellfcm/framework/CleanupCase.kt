package com.nutshellfcm.framework

import com.nutshellfcm.framework.model.NotificationMessage


internal class CleanupCase : NutshellFCMContract.Case {

    override val actionIds: List<String> = emptyList()

    override fun consume(caseMessages: List<NotificationMessage>) {}
}