package com.nutshellfcm.framework

import android.app.Application
import android.util.Log
import com.nutshellfcm.framework.di.NutshellFirebaseComponents
import com.nutshellfcm.framework.extensions.TAG
import com.nutshellfcm.common.PersistentAdapterContract
import com.google.firebase.iid.FirebaseInstanceId


object NutshellFCMEngine {

    var isInitialized = false
    private set

    var firebaseToken = ""

    val firebaseInstanceId = FirebaseInstanceId.getInstance()

    fun start(application: Application,
              androidNotificationsFactory: NutshellFCMContract.AndroidNotificationsFactory = DefaultNotificationFactory(),
              casesProvider: NutshellFCMContract.CasesProvider = DefaultCaseProvider(),
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