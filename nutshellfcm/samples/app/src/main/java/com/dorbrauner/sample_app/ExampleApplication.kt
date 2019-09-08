package com.dorbrauner.sample_app

import android.app.Application
import com.dorbrauner.nutshellfirebase.NutshellFirebaseEngine

class ExampleApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        NutshellFirebaseEngine.start(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider(),
                            ExampleForegroundServicesBinder())
    }


}