package com.dorbrauner.checknutshellfirebase

import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage


class MyNotificationFactory : NutshellFirebaseContract.AndroidNotificationsFactory {

    override fun create(notificationMessage: NotificationMessage): NutshellFirebaseContract.AndroidNotification {
        return NutshellFirebaseContract.AndroidNotification(
            id = 1,
            payload = notificationMessage.payload,
            contentTitle = "test title",
            contentText = "test description",
            importance = NutshellFirebaseContract.Importance.HIGH,
            channel = NutshellFirebaseContract.NotificationChannel("channel", "name", null),
            smallIcon = android.R.drawable.alert_light_frame
        )
    }
}