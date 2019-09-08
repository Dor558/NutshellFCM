package com.dorbrauner.nutshellfcm

import android.app.Application
import android.util.Log
import com.dorbrauner.nutshellfcm.di.NutshellFirebaseComponents
import com.dorbrauner.nutshellfcm.extensions.TAG
import com.dorbrauner.persistentadapters.PersistentAdapterContract
import com.google.firebase.iid.FirebaseInstanceId


object NutshellFCMEngine {

    var isInitialized = false
    private set

    var firebaseToken = ""

    val firebaseInstanceId = FirebaseInstanceId.getInstance()

    fun start(application: Application,
              androidNotificationsFactory: NutshellFCMContract.AndroidNotificationsFactory,
              casesProvider: NutshellFCMContract.NotificationsHandling.CasesProvider,
              foregroundServicesBinder: NutshellFCMContract.ForegroundServicesBinder = DefaultForegroundServicesBinder(),
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