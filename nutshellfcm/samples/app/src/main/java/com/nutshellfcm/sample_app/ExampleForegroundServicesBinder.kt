package com.nutshellfcm.sample_app

import com.nutshellfcm.framework.NutshellFCMContract


class ExampleForegroundServicesBinder : NutshellFCMContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? {
        return when (actionId) {
            "Action 4" -> ExampleForegroundService::class.java
            else -> null
        }
    }
}