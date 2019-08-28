package com.dorbrauner.framework

import android.app.Application
import android.util.Log
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.dorbrauner.framework.extensions.TAG
import com.google.firebase.iid.FirebaseInstanceId


object NutshellFirebase {

    var isInitialized = false
    private set

    var firebaseToken = ""

    val firebaseInstanceId = FirebaseInstanceId.getInstance()

    fun start(application: Application,
              androidNotificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory,
              casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider,
              foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder = DefaultForegroundServicesBinder()) {

        if (isInitialized) {
            return
        }

        isInitialized = FirebaseMessagingComponents.init(
            application,
            androidNotificationsFactory,
            casesProvider,
            foregroundServicesBinder
        )

        firebaseInstanceId.instanceId.addOnSuccessListener { instanceIdResult ->
            firebaseToken = instanceIdResult.token
            Log.d(TAG, "FirebaseToken = $firebaseToken")
        }
    }
}