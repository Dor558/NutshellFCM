package com.dorbrauner.example_app

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.database.model.NotificationMessage


class ExampleNotificationFactory(private val application: Application) : NotificationsFrameworkContract.AndroidNotificationsFactory {

    override fun create(notificationMessage: NotificationMessage): NotificationsFrameworkContract.AndroidNotification {
        return when (notificationMessage.actionId) {
            "Action 1" -> {
                NotificationsFrameworkContract.AndroidNotification(
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
                    importance = NotificationsFrameworkContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NotificationsFrameworkContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            "Action 2" -> {
                NotificationsFrameworkContract.AndroidNotification(
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
                    importance = NotificationsFrameworkContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NotificationsFrameworkContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            "Action 3" -> {
                NotificationsFrameworkContract.AndroidNotification(
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
                    importance = NotificationsFrameworkContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NotificationsFrameworkContract.NotificationChannel("example channel id",
                        "example channel",
                        null)
                )
            }

            "Action 4" -> {
                NotificationsFrameworkContract.AndroidNotification(
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
                    importance = NotificationsFrameworkContract.Importance.HIGH,
                    smallIcon = android.R.drawable.gallery_thumb,
                    channel = NotificationsFrameworkContract.NotificationChannel("example channel id",
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