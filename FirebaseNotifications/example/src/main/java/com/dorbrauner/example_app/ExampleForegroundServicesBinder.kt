package com.dorbrauner.example_app

import com.dorbrauner.framework.NotificationsFrameworkContract


class ExampleForegroundServicesBinder : NotificationsFrameworkContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? {
        return when (actionId) {
            "Action 4" -> ExampleForegroundService::class.java
            else -> null
        }
    }
}