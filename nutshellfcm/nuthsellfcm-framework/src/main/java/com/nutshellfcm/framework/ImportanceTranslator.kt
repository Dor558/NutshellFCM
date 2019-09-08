package com.nutshellfcm.framework

import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationManagerCompat.*
import com.nutshellfcm.framework.NutshellFCMContract.Importance.*

internal class ImportanceTranslator: NutshellFCMContract.Translators.ImportanceTranslator {

    override fun getAndroidPriority(importance: NutshellFCMContract.Importance) =
            when(importance) {
                LOW -> PRIORITY_MIN
                MEDIUM -> PRIORITY_LOW
                HIGH -> PRIORITY_DEFAULT
                URGENT -> PRIORITY_MAX
            }

    override fun getAndroidImportance(importance: NutshellFCMContract.Importance) =
            when(importance) {
                LOW -> IMPORTANCE_MIN
                MEDIUM -> IMPORTANCE_LOW
                HIGH -> IMPORTANCE_DEFAULT
                URGENT -> IMPORTANCE_HIGH
            }
}