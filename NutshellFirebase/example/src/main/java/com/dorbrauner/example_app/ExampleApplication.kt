package com.dorbrauner.example_app

import android.app.Application
import com.dorbrauner.framework.NutshellFirebase


class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NutshellFirebase.start(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider(),
                            ExampleForegroundServicesBinder())
    }

}