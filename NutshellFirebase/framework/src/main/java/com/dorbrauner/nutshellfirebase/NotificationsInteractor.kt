package com.dorbrauner.nutshellfirebase

import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.rxworkframework.RxWork
import com.dorbrauner.rxworkframework.scheudlers.Schedulers
import com.dorbrauner.rxworkframework.works.ScheduledWork


internal class NotificationsInteractor(
    private val persistentSource: NutshellFirebaseContract.Sources.PersistentSource,
    private val cacheSource: NutshellFirebaseContract.Sources.CacheSource
) : NutshellFirebaseContract.Interactor {

    override fun purgeNotifications(): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            cacheSource.clearCache()
            persistentSource.purge()
                    .subscribeOn(Schedulers.unbounded)
                    .subscribe(
                            onResult = {
                                emitter.onResult(Unit)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun removeNotification(id: String): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            cacheSource.removeFromCache(id)
            persistentSource.remove(id)
                    .subscribeOn(Schedulers.unbounded)
                    .subscribe(
                            onResult = {
                                emitter.onResult(Unit)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun removeNotifications(ids: List<String>): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            cacheSource.removeFromCache(ids)
            persistentSource.remove(ids)
                .subscribeOn(Schedulers.unbounded)
                .subscribe(
                    onResult = {
                        emitter.onResult(Unit)
                    },
                    onError = {
                        emitter.onError(it)
                    }
                )
        }
    }

    override fun readNotification(id: String): ScheduledWork<NotificationMessage> {
        return RxWork.create { emitter ->
            val cacheNotificationMessage = cacheSource.readCache(id)
            if (cacheNotificationMessage != null) {
                emitter.onResult(cacheNotificationMessage)
                return@create
            }

            persistentSource.read(id)
                .subscribeOn(Schedulers.unbounded)
                .subscribe(
                    onResult = {
                        emitter.onResult(it)
                    },
                    onError = {
                        emitter.onError(it)
                    }
                )
        }
    }

    override fun readNotifications(): ScheduledWork<List<NotificationMessage>> {
        return RxWork.create { emitter ->
            val cacheNotificationMessage = cacheSource.readCache()
            if (!cacheNotificationMessage.isNullOrEmpty()) {
                emitter.onResult(cacheNotificationMessage)
                return@create
            }

            persistentSource.read()
                    .subscribeOn(Schedulers.unbounded)
                    .subscribe(
                            onResult = {
                                emitter.onResult(it)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun writeNotification(notificationMessage: NotificationMessage): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            cacheSource.writeCache(notificationMessage)
            persistentSource.write(notificationMessage)
                    .subscribeOn(Schedulers.unbounded)
                    .subscribe(
                            onResult = {
                                emitter.onResult(Unit)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }
}