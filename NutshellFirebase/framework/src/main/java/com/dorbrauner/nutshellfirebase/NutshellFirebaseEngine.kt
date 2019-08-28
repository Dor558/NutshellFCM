package com.dorbrauner.nutshellfirebase

import android.app.Application
import android.util.Log
import com.dorbrauner.nutshellfirebase.di.FirebaseMessagingComponents
import com.dorbrauner.nutshellfirebase.extensions.TAG
import com.google.firebase.iid.FirebaseInstanceId


object NutshellFirebaseEngine {

    var isInitialized = false
    private set

    var firebaseToken = ""

    val firebaseInstanceId = FirebaseInstanceId.getInstance()

    fun start(application: Application,
              androidNotificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory,
              casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider,
              foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder = DefaultForegroundServicesBinder()) {

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