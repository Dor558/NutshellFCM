package com.dorbrauner.example_app

import com.dorbrauner.example_app.notifications_use_cases.Action1ExampleCase
import com.dorbrauner.example_app.notifications_use_cases.Action2Action3ExampleCase
import com.dorbrauner.example_app.notifications_use_cases.Action4ExampleCase
import com.dorbrauner.framework.NotificationsFrameworkContract

class ExampleCaseProvider : NotificationsFrameworkContract.NotificationsHandling.CasesProvider {

    override val cases: List<NotificationsFrameworkContract.NotificationsHandling.Case> =
        listOf(Action1ExampleCase(), Action2Action3ExampleCase(), Action4ExampleCase())
}