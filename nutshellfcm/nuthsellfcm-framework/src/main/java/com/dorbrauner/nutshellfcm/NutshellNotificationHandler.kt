package com.dorbrauner.nutshellfcm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dorbrauner.nutshellfcm.model.NotificationMessage


object NutshellNotificationHandler : NutshellFCMContract.NotificationsHandling.HandledNotificationsNotifier {

    override val handledNotifications: LiveData<List<NotificationMessage>> = MutableLiveData()
}