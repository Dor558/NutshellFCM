package com.nutshellfcm.framework

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.common.PersistentAdapterContract.*
import com.nutshellfcm.rxworkframework.works.ScheduledWork

interface NutshellFCMContract {

    companion object {
        const val KEY_ACTION_ID = "action_id"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_PAYLOAD = "payload"
        const val KEY_TYPE = "type"
        const val ACTION_BROADCAST_REGISTRATION_NOTIFICATION = "ACTION_BROADCAST_REGISTRATION_NOTIFICATION"
    }

    interface NotificationMessageWriter {
        fun write(notificationMessage: NotificationMessage): ScheduledWork<Unit>
    }

    interface NotificationMessageReader {
        fun read(): ScheduledWork<List<NotificationMessage>>
        fun read(id: String): ScheduledWork<NotificationMessage>
    }

    interface NotificationsNotifier {
        fun notifyMessage(notificationMessage: NotificationMessage)
    }

    interface NotificationsMessageRouter {
        fun onRouteNotificationsMessage(intent: Intent)
    }

    interface Repository : NotificationMessageReader {
        fun purge(): ScheduledWork<Unit>
        fun remove(id: String): ScheduledWork<Unit>
        fun remove(ids: List<String>): ScheduledWork<Unit>

    }

    interface Sources {

        interface PersistedSource {

            fun purge(): ScheduledWork<Unit>
            fun remove(id: String): ScheduledWork<Unit>
            fun remove(ids: List<String>): ScheduledWork<Unit>
            fun read(): ScheduledWork<List<NotificationMessage>>
            fun read(id: String): ScheduledWork<NotificationMessage>
            fun write(notificationMessage: NotificationMessage): ScheduledWork<Unit>

            interface PersistedMessageToNotificationMessageConverter {

                fun convert(persistNotificationMessage: PersistNotificationMessage): NotificationMessage
                fun convert(persistNotificationMessages: List<PersistNotificationMessage>): List<NotificationMessage>
                fun convertBack(notificationMessage: NotificationMessage): PersistNotificationMessage
                fun convertBack(notificationMessages: List<NotificationMessage>): List<PersistNotificationMessage>
            }

        }

        interface CacheSource {

            fun clearCache()
            fun removeFromCache(id: String)
            fun removeFromCache(ids: List<String>)
            fun readCache(): List<NotificationMessage>?
            fun readCache(id: String): NotificationMessage?
            fun writeCache(notificationMessage: NotificationMessage)
        }
    }

    interface Interactor {

        fun purgeNotifications(): ScheduledWork<Unit>
        fun removeNotification(id: String): ScheduledWork<Unit>
        fun removeNotifications(ids: List<String>): ScheduledWork<Unit>
        fun readNotification(id: String): ScheduledWork<NotificationMessage>
        fun readNotifications(): ScheduledWork<List<NotificationMessage>>
        fun writeNotification(notificationMessage: NotificationMessage): ScheduledWork<Unit>
    }

    interface NotificationsHandling {

        interface CasesProvider {
            val cases: List<Case>
        }

        interface CasesManager {
            fun init()
            fun hasRemainingCases(): Boolean
            fun handleNextCase(notifications: List<NotificationMessage>): List<NotificationMessage>
        }

        interface Case {

            val actionIds: List<String>

            fun consume(caseMessages: List<NotificationMessage>)
        }

        interface HandledNotificationsNotifier {
            val handledNotifications: LiveData<List<NotificationMessage>>
        }
    }

    interface NotificationsConsumer {
        fun consume(actionId: String)
        fun consumeNotificationsMessages()
    }

    interface AndroidNotificationsManager {
        fun show(context: Context, androidNotification: AndroidNotification)
        fun showForeground(service: Service, androidNotification: AndroidNotification)
    }

    interface AndroidNotificationsFactory {
        fun create(notificationMessage: NotificationMessage): AndroidNotification
    }

    interface ForegroundServicesBinder {
        fun bind(actionId: String) : Class<*>?
    }

    interface AndroidNotificationBuilder {
        fun build(notificationMessage: NotificationMessage)
        fun buildForeground(notificationMessage: NotificationMessage)
    }

    interface NotificationReceiversRegisterer {

        companion object {
            const val BACKGROUND_PRIORITY = 0
            const val FOREGROUND_PRIORITY = 1
        }

    }

    data class AndroidNotification(
        val id: Int,
        val payload: Map<String, String> = emptyMap(),
        val contentTitle: CharSequence,
        val contentText: CharSequence,
        val color: Int = -1,
        val largeIcon: Bitmap? = null,
        val smallIcon: Int,
        val contentIntent: PendingIntent? = null,
        val deleteIntent: PendingIntent? = null,
        val importance: Importance,
        val channel: NotificationChannel
    )

    enum class Importance {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    interface Translators {
        interface ImportanceTranslator {
            fun getAndroidPriority(importance: Importance): Int

            fun getAndroidImportance(importance: Importance): Int
        }
    }

    data class NotificationChannel(
        val id: String,
        val channelName: String,
        val soundUri: Uri?
    )

    enum class NotificationType(val value: String) {
        NOTIFICATION("notification"),
        SILENT_NOTIFICATION("silent"),
        FOREGROUND_NOTIFICATION("foreground");

        companion object {
            fun NotificationType(value: String): NotificationType = values().find { it.value == value } ?: NOTIFICATION

        }
    }


    sealed class Error(msg: String) : Throwable(msg) {
        data class UnknownNotificationActionIdThrowable(val actionId: String? = null) : Error("Unknown action id $actionId")
        data class UnknownServiceBindActionIdThrowable(val actionId: String? = null) : kotlin.Error("Unknown foreground service bind for action id $actionId")
    }
}

