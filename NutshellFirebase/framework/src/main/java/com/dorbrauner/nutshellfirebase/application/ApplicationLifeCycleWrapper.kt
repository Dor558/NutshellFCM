package com.dorbrauner.nutshellfirebase.application

import android.app.Application

internal class ApplicationLifeCycleWrapper(private val application: Application) {

    fun registerLifecycle(activityLifecycleCallback: ActivityLifecycleCallback) {
        application.registerActivityLifecycleCallbacks(activityLifecycleCallback)
    }

    fun unregisterLifeCycle(activityLifecycleCallback: ActivityLifecycleCallback) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallback)
    }

}