package com.dorbrauner.nutshellfirebase.sources

import com.dorbrauner.nutshellfirebase.database.Database
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.database.dao.NotificationMessageDao
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import io.reactivex.Completable
import io.reactivex.Single


internal class PersistentSource(Database: Database): NutshellFirebaseContract.Sources.PersistentSource {

    private val dao: NotificationMessageDao = Database.notificationMessageDao()

    override fun purge(): Completable = dao.deleteAll()

    override fun remove(id: String): Completable = dao.deleteByActionId(id)

    override fun remove(ids: List<String>): Completable = dao.deleteGroup(ids)

    override fun read(): Single<List<NotificationMessage>> = dao.getAll()

    override fun read(id: String): Single<NotificationMessage> = dao.getById(id)

    override fun write(notificationMessage: NotificationMessage): Completable = dao.insert(notificationMessage)
}