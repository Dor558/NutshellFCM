package com.dorbrauner.nutshellfirebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dorbrauner.nutshellfirebase.model.NotificationMessage


object NutshellNotificationHandler : NutshellFirebaseContract.NotificationsHandling.HandledNotificationsNotifier {

    override val handledNotifications: LiveData<List<NotificationMessage>> = MutableLiveData()
}