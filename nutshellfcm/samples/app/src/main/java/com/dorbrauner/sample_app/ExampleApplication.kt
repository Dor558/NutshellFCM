package com.dorbrauner.sample_app

import android.app.Application
import com.dorbrauner.nutshellfcm.NutshellFCMEngine

class ExampleApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        NutshellFCMEngine.start(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider(),
                            ExampleForegroundServicesBinder())
    }


}