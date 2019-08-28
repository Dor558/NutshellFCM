package com.dorbrauner.example_app

import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.dorbrauner.framework.application.ForegroundService


class ExampleForegroundService : ForegroundService() {

    override fun onForegroundStarted(payload: Bundle) {
        //Do something with the payload
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}