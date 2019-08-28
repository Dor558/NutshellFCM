package com.dorbrauner.framework.sources

import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.database.model.NotificationMessage

class CacheSource : NotificationsFrameworkContract.Sources.CacheSource {

    private val map: MutableMap<String, NotificationMessage> = mutableMapOf()

    override fun clearCache() {
        map.clear()
    }

    @Synchronized
    override fun removeFromCache(id: String) {
        map.remove(id)
    }

    override fun removeFromCache(ids: List<String>) {
        ids.forEach { map.remove(it) }
    }

    @Synchronized
    override fun readCache(): List<NotificationMessage>? {
        return map.values.toList()
    }

    @Synchronized
    override fun readCache(id: String): NotificationMessage? {
        return map[id]
    }

    @Synchronized
    override fun writeCache(notificationMessage: NotificationMessage) {
        map[notificationMessage.actionId] = notificationMessage
    }
}