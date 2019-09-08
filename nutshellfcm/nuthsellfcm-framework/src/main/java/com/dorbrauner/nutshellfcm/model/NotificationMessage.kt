package com.dorbrauner.nutshellfcm.model

import com.dorbrauner.nutshellfcm.NutshellFCMContract.Companion.KEY_ACTION_ID
import com.dorbrauner.nutshellfcm.NutshellFCMContract.NotificationType
import java.util.*

data class NotificationMessage(

    val actionId: String,

    val type: NotificationType = NotificationType.NOTIFICATION,

    val payload: Map<String, String> = mapOf(KEY_ACTION_ID to actionId),

    val timeStamp: Date = Date()
) {

    val notificationId = actionId.hashCode()
}