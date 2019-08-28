package com.dorbrauner.example_app

import com.dorbrauner.example_app.notifications_use_cases.Action1ExampleCase
import com.dorbrauner.example_app.notifications_use_cases.Action2Action3ExampleCase
import com.dorbrauner.example_app.notifications_use_cases.Action4ExampleCase
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract

class ExampleCaseProvider : NutshellFirebaseContract.NotificationsHandling.CasesProvider {

    override val cases: List<NutshellFirebaseContract.NotificationsHandling.Case> =
        listOf(Action1ExampleCase(), Action2Action3ExampleCase(), Action4ExampleCase())
}