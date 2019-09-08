package com.nutshellfcm.sample_app

import android.app.Application
import com.nutshellfcm.framework.NutshellFCMEngine

class ExampleApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        NutshellFCMEngine.start(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider(),
                            ExampleForegroundServicesBinder())
    }


}