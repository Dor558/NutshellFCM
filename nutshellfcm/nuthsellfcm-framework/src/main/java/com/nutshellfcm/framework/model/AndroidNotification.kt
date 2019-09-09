package com.nutshellfcm.framework.model

import android.app.PendingIntent
import android.graphics.Bitmap
import com.nutshellfcm.framework.NutshellFCMContract

data class AndroidNotification(
    val payload: Map<String, String> = emptyMap(),
    val contentTitle: CharSequence,
    val contentText: CharSequence,
    val color: Int = -1,
    val largeIcon: Bitmap? = null,
    val smallIcon: Int,
    val contentIntent: PendingIntent? = null,
    val deleteIntent: PendingIntent? = null,
    val importance: NutshellFCMContract.Importance,
    val channel: NutshellFCMContract.NotificationChannel
)