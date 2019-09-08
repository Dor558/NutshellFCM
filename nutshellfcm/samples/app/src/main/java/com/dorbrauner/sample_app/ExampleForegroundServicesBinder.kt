package com.dorbrauner.sample_app

import com.dorbrauner.nutshellfcm.NutshellFCMContract


class ExampleForegroundServicesBinder : NutshellFCMContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? {
        return when (actionId) {
            "Action 4" -> ExampleForegroundService::class.java
            else -> null
        }
    }
}