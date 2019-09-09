package com.nutshellfcm.sample_app.notifications_use_cases

import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.model.NotificationMessage


class Action4ExampleCase : NutshellFCMContract.Case {

    override val actionIds: List<String> = listOf("Action 4")

    override fun consume(caseMessages: List<NotificationMessage>) {
        //Do something
    }
}