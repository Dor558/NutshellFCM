package com.nutshellfcm.framework

import com.nutshellfcm.framework.model.AndroidNotification
import com.nutshellfcm.framework.model.NotificationMessage


class DefaultNotificationFactory : NutshellFCMContract.AndroidNotificationsFactory {

    override fun create(notificationMessage: NotificationMessage): AndroidNotification {
        return AndroidNotification(
            payload = notificationMessage.payload,
            contentIntent = null,
            contentTitle = "Example notification title",
            contentText = "Override AndroidNotificationFactory to custom your own notification",
            importance = NutshellFCMContract.Importance.HIGH,
            smallIcon = android.R.drawable.gallery_thumb,
            channel = NutshellFCMContract.NotificationChannel(
                "example channel id",
                "example channel",
                null
            )
        )
    }
}