package com.nutshellfcm.framework

import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.rxworkframework.RxWorkCreator
import com.nutshellfcm.rxworkframework.scheudlers.Schedulers
import com.nutshellfcm.rxworkframework.works.ScheduledWork


internal class NotificationsInteractor(
    private val persistedSource: NutshellFCMContract.Sources.PersistedSource?,
    private val cacheSource: NutshellFCMContract.Sources.CacheSource
) : NutshellFCMContract.Interactor {

    override fun purgeNotifications(): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            cacheSource.clearCache()
            persistedSource?.apply {
                purge()
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

    override fun removeNotification(id: String): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            cacheSource.removeFromCache(id)
            persistedSource?.apply {
                remove(id).subscribe(
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

    override fun removeNotifications(ids: List<String>): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            cacheSource.removeFromCache(ids)
            persistedSource?.apply {
                remove(ids)
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

    override fun readNotification(id: String): ScheduledWork<NotificationMessage> {
        return RxWorkCreator.create { emitter ->
            val cacheNotificationMessage = cacheSource.readCache(id)
            if (cacheNotificationMessage != null) {
                emitter.onResult(cacheNotificationMessage)
                return@create
            }

            persistedSource?.apply {
                read(id)
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
    }

    override fun readNotifications(): ScheduledWork<List<NotificationMessage>> {
        return RxWorkCreator.create { emitter ->
            val cacheNotificationMessage = cacheSource.readCache()
            if (!cacheNotificationMessage.isNullOrEmpty()) {
                emitter.onResult(cacheNotificationMessage)
                return@create
            }

            persistedSource?.apply {
                read()
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
    }

    override fun writeNotification(notificationMessage: NotificationMessage): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            cacheSource.writeCache(notificationMessage)
            persistedSource?.apply {
                write(notificationMessage)
                    .subscribeOn(Schedulers.single)
                    .subscribe(
                        onResult = {
                            emitter.onResult(Unit)
                        },
                        onError = {
                            emitter.onError(it)
                        }
                    )
            } ?: emitter.onResult(Unit)
        }
    }
}