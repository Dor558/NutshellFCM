package com.dorbrauner.framework

import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationManagerCompat.*
import com.dorbrauner.framework.NotificationsFrameworkContract.Importance.*

internal class ImportanceTranslator: NotificationsFrameworkContract.Translators.ImportanceTranslator {

    override fun getAndroidPriority(importance: NotificationsFrameworkContract.Importance) =
            when(importance) {
                LOW -> PRIORITY_MIN
                MEDIUM -> PRIORITY_LOW
                HIGH -> PRIORITY_DEFAULT
                URGENT -> PRIORITY_MAX
            }

    override fun getAndroidImportance(importance: NotificationsFrameworkContract.Importance) =
            when(importance) {
                LOW -> IMPORTANCE_MIN
                MEDIUM -> IMPORTANCE_LOW
                HIGH -> IMPORTANCE_DEFAULT
                URGENT -> IMPORTANCE_HIGH
            }
}