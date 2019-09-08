package com.nutshellfcm.framework.model

import com.nutshellfcm.framework.NutshellFCMContract.Companion.KEY_ACTION_ID
import com.nutshellfcm.framework.NutshellFCMContract.NotificationType
import java.util.*

data class NotificationMessage(

    val actionId: String,

    val type: NotificationType = NotificationType.NOTIFICATION,

    val payload: Map<String, String> = mapOf(KEY_ACTION_ID to actionId),

    val timeStamp: Date = Date()
) {

    val notificationId = actionId.hashCode()
}