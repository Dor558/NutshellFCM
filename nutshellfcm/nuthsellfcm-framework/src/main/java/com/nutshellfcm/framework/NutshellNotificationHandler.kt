package com.nutshellfcm.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nutshellfcm.framework.model.NotificationMessage


object NutshellNotificationHandler : NutshellFCMContract.NotificationsHandling.HandledNotificationsNotifier {

    override val handledNotifications: LiveData<List<NotificationMessage>> = MutableLiveData()
}