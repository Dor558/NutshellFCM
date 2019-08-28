package com.dorbrauner.nutshellfirebase

import androidx.core.app.NotificationCompat.*
import androidx.core.app.NotificationManagerCompat.*
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract.Importance.*

internal class ImportanceTranslator: NutshellFirebaseContract.Translators.ImportanceTranslator {

    override fun getAndroidPriority(importance: NutshellFirebaseContract.Importance) =
            when(importance) {
                LOW -> PRIORITY_MIN
                MEDIUM -> PRIORITY_LOW
                HIGH -> PRIORITY_DEFAULT
                URGENT -> PRIORITY_MAX
            }

    override fun getAndroidImportance(importance: NutshellFirebaseContract.Importance) =
            when(importance) {
                LOW -> IMPORTANCE_MIN
                MEDIUM -> IMPORTANCE_LOW
                HIGH -> IMPORTANCE_DEFAULT
                URGENT -> IMPORTANCE_HIGH
            }
}