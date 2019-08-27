package com.dorbrauner.example_app.notifications_use_cases

import com.dorbrauner.framework.NotificationsFrameworkContract


class Action4ExampleCase : NotificationsFrameworkContract.NotificationsHandling.Case {

    override val actionIds: List<String> = listOf("Action 4")

    override fun consume(caseMessages: List<NotificationsFrameworkContract.Repository.NotificationMessage>) {
        //Do something
    }
}