package com.nutshellfcm.sample_app

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.model.NotificationMessage


class ExampleNotificationFactory(private val application: Application) : NutshellFCMContract.AndroidNotificationsFactory {

    override fun create(notificationMessage: NotificationMessage): NutshellFCMContract.AndroidNotification {
        return when (notificationMessage.actionId) {
            "Action 1" -> {
                NutshellFCMContract.AndroidNotification(
                    id = notificationMessage.notificationId,
                    payload = notificationMessage.payload,
                    contentIntent = PendingIntent.getActivity(
                        application,
                        0,
                        Intent(application, ExampleActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    ),
                    contentTitle = "notification type 1",
                    contentText = "This is an example content",
                    importance = NutshellFCMContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NutshellFCMContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            "Action 2" -> {
                NutshellFCMContract.AndroidNotification(
                    id = notificationMessage.notificationId,
                    payload = notificationMessage.payload,
                    contentIntent = PendingIntent.getActivity(
                        application,
                        0,
                        Intent(application, ExampleActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    ),
                    contentTitle = "notification type 2",
                    contentText = "This is an example content",
                    importance = NutshellFCMContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NutshellFCMContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            "Action 3" -> {
                NutshellFCMContract.AndroidNotification(
                    id = notificationMessage.notificationId,
                    payload = notificationMessage.payload,
                    contentIntent = PendingIntent.getActivity(
                        application,
                        0,
                        Intent(application, ExampleActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    ),
                    contentTitle = "notification type 3",
                    contentText = "This is an example content",
                    importance = NutshellFCMContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NutshellFCMContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            "Action 4" -> {
                NutshellFCMContract.AndroidNotification(
                    id = notificationMessage.notificationId,
                    payload = notificationMessage.payload,
                    contentIntent = PendingIntent.getActivity(
                        application,
                        0,
                        Intent(application, ExampleActivity::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    ),
                    contentTitle = "notification type 4 (foreground)",
                    contentText = "This is an example content",
                    importance = NutshellFCMContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NutshellFCMContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            else -> {
                throw Throwable("Unsupported notification id")
            }
        }
    }
}