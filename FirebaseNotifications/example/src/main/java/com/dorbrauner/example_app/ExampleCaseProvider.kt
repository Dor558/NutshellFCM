package com.dorbrauner.example_app

import com.dorbrauner.example_app.notifications_use_cases.Action1Case
import com.dorbrauner.example_app.notifications_use_cases.Action2Case
import com.dorbrauner.framework.NotificationsFrameworkContract

class ExampleCaseProvider : NotificationsFrameworkContract.NotificationsHandling.CasesProvider {

    override val cases: List<NotificationsFrameworkContract.NotificationsHandling.Case>
        get() = listOf(Action1Case(), Action2Case())
}