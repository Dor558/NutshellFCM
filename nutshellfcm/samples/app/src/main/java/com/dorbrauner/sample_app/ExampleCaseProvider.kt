package com.dorbrauner.sample_app

import com.dorbrauner.sample_app.notifications_use_cases.Action1ExampleCase
import com.dorbrauner.sample_app.notifications_use_cases.Action2Action3ExampleCase
import com.dorbrauner.sample_app.notifications_use_cases.Action4ExampleCase
import com.dorbrauner.nutshellfcm.NutshellFCMContract

class ExampleCaseProvider : NutshellFCMContract.NotificationsHandling.CasesProvider {

    override val cases: List<NutshellFCMContract.NotificationsHandling.Case> =
        listOf(Action1ExampleCase(), Action2Action3ExampleCase(), Action4ExampleCase())
}