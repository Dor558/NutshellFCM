package com.nutshellfcm.common

import java.util.*


class PersistentAdapterContract {


    interface Adapter {

        fun insert(notificationMessages: List<PersistNotificationMessage>)
        fun insert(notificationMessage: PersistNotificationMessage)
        fun get(actionId: String): PersistNotificationMessage
        fun get(): List<PersistNotificationMessage>
        fun remove(actionId: String)
        fun removeGroup(actionIds: List<String>)
        fun purge()
    }

    interface PersistNotificationMessage {
        val actionId: String
        val type: String
        val payload: Map<String, String>
        val timeStamp: Date
    }

}