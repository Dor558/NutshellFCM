package com.dorbrauner.framework.sources

import com.dorbrauner.framework.database.AppDatabase
import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.database.model.NotificationMessageDao
import io.reactivex.Completable
import io.reactivex.Single


internal class PersistentSource(appDatabase: AppDatabase): NotificationsFrameworkContract.Sources.PersistentSource {

    private val dao: NotificationMessageDao = appDatabase.notificationMessageDao()

    override fun purge(): Completable = dao.deleteAll()

    override fun remove(id: String): Completable = dao.deleteByActionId(id)

    override fun read(): Single<List<NotificationsFrameworkContract.Repository.NotificationMessage>> = dao.getAll()

    override fun read(id: String): Single<NotificationsFrameworkContract.Repository.NotificationMessage> = dao.getById(id)

    override fun write(notificationMessage: NotificationsFrameworkContract.Repository.NotificationMessage): Completable = dao.insert(notificationMessage)
}