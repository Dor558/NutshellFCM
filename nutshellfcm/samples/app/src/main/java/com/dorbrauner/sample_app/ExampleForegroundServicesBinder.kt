package com.dorbrauner.sample_app

import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract


class ExampleForegroundServicesBinder : NutshellFirebaseContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? {
        return when (actionId) {
            "Action 4" -> ExampleForegroundService::class.java
            else -> null
        }
    }
}