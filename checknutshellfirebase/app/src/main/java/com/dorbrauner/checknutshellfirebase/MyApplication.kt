package com.dorbrauner.checknutshellfirebase

import android.app.Application
import com.dorbrauner.nutshellfirebase.NutshellFirebaseEngine


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NutshellFirebaseEngine.start(
            this, MyNotificationFactory(), MyCaseProvider()
        )
    }

}