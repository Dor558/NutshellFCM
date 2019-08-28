package com.dorbrauner.example_app

import android.app.Application
import com.dorbrauner.framework.FirebaseEngine


class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseEngine.start(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider(),
                            ExampleForegroundServicesBinder())
    }

}