package com.dorbrauner.framework

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.dorbrauner.framework.database.model.NotificationMessage
import io.reactivex.Completable
import io.reactivex.Single

interface NotificationsFrameworkContract {

    companion object {
        const val KEY_ACTION_ID = "action_id"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_PAYLOAD = "payload"
        const val KEY_TYPE = "type"
        const val ACTION_BROADCAST_REGISTRATION_NOTIFICATION = "ACTION_BROADCAST_REGISTRATION_NOTIFICATION"
    }

    interface NotificationMessageWriter {
        fun write(notificationMessage: NotificationMessage): Completable
    }

    interface NotificationMessageReader {
        fun read(): Single<List<NotificationMessage>>
        fun read(id: String): Single<NotificationMessage>
    }

    interface NotificationsNotifier {
        fun notifyMessage(notificationMessage: NotificationMessage)
    }

    interface NotificationsMessageRouter {
        fun onRouteNotificationsMessage(intent: Intent)
    }

    interface Repository : NotificationMessageReader {
        fun purge(): Completable
        fun remove(id: String): Completable
        fun remove(ids: List<String>): Completable

    }

    interface Sources {

        interface PersistentSource {

            companion object {
                const val ROOM_TABLE_NOTIFICATION_MESSAGE = "notification_messages"
            }

            fun purge(): Completable
            fun remove(id: String): Completable
            fun remove(ids: List<String>): Completable
            fun read(): Single<List<NotificationMessage>>
            fun read(id: String): Single<NotificationMessage>
            fun write(notificationMessage: NotificationMessage): Completable

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

        fun purgeNotifications(): Completable
        fun removeNotification(id: String): Completable
        fun removeNotifications(ids: List<String>): Completable
        fun readNotification(id: String): Single<NotificationMessage>
        fun readNotifications(): Single<List<NotificationMessage>>
        fun writeNotification(notificationMessage: NotificationMessage): Completable
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

