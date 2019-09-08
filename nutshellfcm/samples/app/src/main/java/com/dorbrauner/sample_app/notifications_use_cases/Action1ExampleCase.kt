package com.dorbrauner.sample_app.notifications_use_cases

import com.dorbrauner.nutshellfcm.NutshellFCMContract
import com.dorbrauner.nutshellfcm.model.NotificationMessage

class Action1ExampleCase : NutshellFCMContract.NotificationsHandling.Case {

    override val actionIds: List<String> = listOf("Action 1")

    override fun consume(caseMessages: List<NotificationMessage>) {
        caseMessages.forEach {
            val payload = it.payload
            //Do something with the payload
        }
    }
}