package com.dorbrauner.framework

import android.app.Application
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.google.firebase.iid.FirebaseInstanceId


object FirebaseEngine {

    var isInitialized = false
    private set

    var firebaseToken = ""

    fun start(application: Application,
              androidNotificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory,
              casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider,
              foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder = DefaultForegroundBinder()) {

        if (isInitialized) {
            return
        }

        isInitialized = FirebaseMessagingComponents.init(
            application,
            androidNotificationsFactory,
            casesProvider,
            foregroundServicesBinder
        )

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            firebaseToken = instanceIdResult.token
        }
    }
}