package com.dorbrauner.nutshellfirebase.model

import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Companion.KEY_ACTION_ID
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.NotificationType
import java.util.*

data class NotificationMessage(

    val actionId: String,

    val type: NotificationType = NotificationType.NOTIFICATION,

    val payload: Map<String, String> = mapOf(KEY_ACTION_ID to actionId),

    val timeStamp: Date = Date()
) {

    val notificationId = actionId.hashCode()
}