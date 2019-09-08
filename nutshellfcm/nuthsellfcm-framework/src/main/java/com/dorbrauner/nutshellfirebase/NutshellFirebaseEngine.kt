package com.dorbrauner.nutshellfirebase

import android.app.Application
import android.util.Log
import com.dorbrauner.nutshellfirebase.di.NutshellFirebaseComponents
import com.dorbrauner.nutshellfirebase.extensions.TAG
import com.dorbrauner.persistentadapters.PersistentAdapterContract
import com.google.firebase.iid.FirebaseInstanceId


object NutshellFirebaseEngine {

    var isInitialized = false
    private set

    var firebaseToken = ""

    val firebaseInstanceId = FirebaseInstanceId.getInstance()

    fun start(application: Application,
              androidNotificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory,
              casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider,
              foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder = DefaultForegroundServicesBinder(),
              persistentAdapter: PersistentAdapterContract.Adapter? = null
    ) {

        if (isInitialized) {
            return
        }

        isInitialized = NutshellFirebaseComponents.init(
            application,
            androidNotificationsFactory,
            casesProvider,
            foregroundServicesBinder,
            persistentAdapter
        )

        firebaseInstanceId.instanceId.addOnSuccessListener { instanceIdResult ->
            firebaseToken = instanceIdResult.token
            Log.d(TAG, "FirebaseToken = $firebaseToken")
        }
    }
}