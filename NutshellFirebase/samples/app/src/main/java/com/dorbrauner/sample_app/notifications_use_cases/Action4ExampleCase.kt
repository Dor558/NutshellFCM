package com.dorbrauner.sample_app.notifications_use_cases

import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.model.NotificationMessage


class Action4ExampleCase : NutshellFirebaseContract.NotificationsHandling.Case {

    override val actionIds: List<String> = listOf("Action 4")

    override fun consume(caseMessages: List<NotificationMessage>) {
        //Do something
    }
}