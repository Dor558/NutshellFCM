package com.nutshellfcm.sample_app.notifications_use_cases

import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.model.NotificationMessage

class Action1ExampleCase : NutshellFCMContract.Case {

    override val actionIds: List<String> = listOf("Action 1")

    override fun consume(caseMessages: List<NotificationMessage>) {
        caseMessages.forEach {
            val payload = it.payload
            //Do something with the payload
        }
    }
}