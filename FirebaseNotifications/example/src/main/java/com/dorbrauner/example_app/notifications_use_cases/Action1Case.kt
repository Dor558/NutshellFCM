package com.dorbrauner.example_app.notifications_use_cases

import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage

class Action1Case : NotificationsFrameworkContract.NotificationsHandling.Case {

    override fun consume(notificationMessages: List<NotificationMessage>): List<NotificationMessage> {
        val consumedNotifications = notificationMessages.filter { it.actionId == "Action 1" }
        consumedNotifications.forEach {
            val payload = it.payload
            //Do something with the payload
        }

        return consumedNotifications
    }
}