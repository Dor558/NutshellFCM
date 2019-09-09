package com.nutshellfcm.sample_app

import com.nutshellfcm.sample_app.notifications_use_cases.Action1ExampleCase
import com.nutshellfcm.sample_app.notifications_use_cases.Action2Action3ExampleCase
import com.nutshellfcm.sample_app.notifications_use_cases.Action4ExampleCase
import com.nutshellfcm.framework.NutshellFCMContract

class ExampleCaseProvider : NutshellFCMContract.CasesProvider {

    override val cases: List<NutshellFCMContract.Case> =
        listOf(Action1ExampleCase(), Action2Action3ExampleCase(), Action4ExampleCase())
}