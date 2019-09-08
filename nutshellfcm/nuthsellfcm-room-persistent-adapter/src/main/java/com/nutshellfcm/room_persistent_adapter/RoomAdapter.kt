package com.nutshellfcm.room_persistent_adapter

import com.nutshellfcm.common.PersistentAdapterContract.*
import com.nutshellfcm.room_persistent_adapter.dao.RoomNotificationMessageDao

internal class RoomAdapter(
    private val converter: RoomAdapterContract.Converter,
    private val roomNotificationMessageDao: RoomNotificationMessageDao
) : Adapter {

    override fun insert(notificationMessages: List<PersistNotificationMessage>) {
        roomNotificationMessageDao.insert(converter.convert(notificationMessages))
    }

    override fun insert(notificationMessage: PersistNotificationMessage) {
        roomNotificationMessageDao.insert(converter.convert(notificationMessage))
    }

    override fun get(actionId: String): PersistNotificationMessage {
        return roomNotificationMessageDao.getById(actionId)
    }

    override fun get(): List<PersistNotificationMessage> {
        return roomNotificationMessageDao.getAll()
    }

    override fun remove(actionId: String) {
        roomNotificationMessageDao.deleteByActionId(actionId)
    }

    override fun removeGroup(actionIds: List<String>) {
        roomNotificationMessageDao.deleteGroup(actionIds)
    }

    override fun purge() {
        roomNotificationMessageDao.deleteAll()
    }
}