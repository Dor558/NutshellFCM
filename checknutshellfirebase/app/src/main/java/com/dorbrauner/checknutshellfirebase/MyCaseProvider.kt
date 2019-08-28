package com.dorbrauner.checknutshellfirebase

import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract


class MyCaseProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider {

    override val cases: List<NutshellFirebaseContract.NotificationsHandling.Case>
        get() = emptyList()
}