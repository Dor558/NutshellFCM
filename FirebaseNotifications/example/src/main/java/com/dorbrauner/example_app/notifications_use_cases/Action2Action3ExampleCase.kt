package com.dorbrauner.example_app.notifications_use_cases

import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage


class Action2Action3ExampleCase : NotificationsFrameworkContract.NotificationsHandling.Case {

    override val actionIds: List<String> = listOf("Action 2", "Action 3")

    override fun consume(caseMessages: List<NotificationMessage>) {
        caseMessages.forEach {
            val payload = it.payload
            //Do something with the payload
        }
    }
}