package com.dorbrauner.nutshellfirebase.sources

import com.dorbrauner.nutshellfirebase.database.Database
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.database.dao.NotificationMessageDao
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.rxworkframework.RxWork
import com.dorbrauner.rxworkframework.works.ScheduledWork


internal class PersistentSource(Database: Database): NutshellFirebaseContract.Sources.PersistentSource {

    private val dao: NotificationMessageDao = Database.notificationMessageDao()

    override fun purge(): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            runCatching {
                emitter.onResult(dao.deleteAll())
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun remove(id: String): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            runCatching {
                emitter.onResult(dao.deleteByActionId(id))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun remove(ids: List<String>): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            runCatching {
                emitter.onResult(dao.deleteGroup(ids))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun read(): ScheduledWork<List<NotificationMessage>> {
        return RxWork.create { emitter ->
            runCatching {
                emitter.onResult(dao.getAll())
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun read(id: String): ScheduledWork<NotificationMessage>{
        return RxWork.create { emitter ->
            runCatching {
                emitter.onResult(dao.getById(id))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun write(notificationMessage: NotificationMessage): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            runCatching {
                dao.insert(notificationMessage)
                emitter.onResult(Unit)
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }
}