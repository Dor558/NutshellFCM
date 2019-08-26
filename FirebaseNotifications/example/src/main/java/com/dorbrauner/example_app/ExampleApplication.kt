package com.dorbrauner.example_app

import android.app.Application
import com.dorbrauner.framework.FirebaseLoader


class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseLoader.load(this,
                            ExampleNotificationFactory(this),
                            ExampleCaseProvider())
    }

}