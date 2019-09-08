package com.nutshellfcm.framework.sources

import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.model.NotificationMessage

class CacheSource : NutshellFCMContract.Sources.CacheSource {

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