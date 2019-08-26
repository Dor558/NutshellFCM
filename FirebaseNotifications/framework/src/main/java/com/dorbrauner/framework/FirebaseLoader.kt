package com.dorbrauner.framework

import android.app.Application
import com.dorbrauner.framework.di.FirebaseMessagingComponents
import com.google.firebase.iid.FirebaseInstanceId


object FirebaseLoader {

    var isInitialized = false
    private set

    var firebaseToken = ""

    fun load(application: Application,
             androidNotificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory,
             casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider) {

        if (isInitialized) {
            return
        }

        isInitialized = FirebaseMessagingComponents.init(application, androidNotificationsFactory, casesProvider)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            firebaseToken = instanceIdResult.token
        }
    }
}