package com.dorbrauner.framework

import com.dorbrauner.framework.database.model.NotificationMessage
import com.dorbrauner.framework.extensions.subscribeBy
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


internal class NotificationsInteractor(
    private val persistentSource: NotificationsFrameworkContract.Sources.PersistentSource,
    private val cacheSource: NotificationsFrameworkContract.Sources.CacheSource
) : NotificationsFrameworkContract.Interactor {

    override fun purgeNotifications(): Completable {
        return Completable.create { emitter ->
            cacheSource.clearCache()
            val disposable = persistentSource.purge()
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                emitter.onComplete()
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )

            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }

    override fun removeNotification(id: String): Completable {
        return Completable.create { emitter ->
            cacheSource.removeFromCache(id)
            val disposable = persistentSource.remove(id)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                emitter.onComplete()
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )


            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }

    override fun removeNotifications(ids: List<String>): Completable {
        return Completable.create { emitter ->
            cacheSource.removeFromCache(ids)
            val disposable = persistentSource.remove(ids)
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onComplete = {
                        emitter.onComplete()
                    },
                    onError = {
                        emitter.onError(it)
                    }
                )


            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }

    override fun readNotification(id: String): Single<NotificationMessage> {
        return Single.create { emitter ->
            val cacheNotificationMessage = cacheSource.readCache(id)
            if (cacheNotificationMessage != null) {
                emitter.onSuccess(cacheNotificationMessage)
                return@create
            }

            val disposable = persistentSource.read(id)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onSuccess = {
                                emitter.onSuccess(it)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )

            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }

    override fun readNotifications(): Single<List<NotificationMessage>> {
        return Single.create { emitter ->
            val cacheNotificationMessage = cacheSource.readCache()
            if (!cacheNotificationMessage.isNullOrEmpty()) {
                emitter.onSuccess(cacheNotificationMessage)
                return@create
            }

            val disposable = persistentSource.read()
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onSuccess = {
                                emitter.onSuccess(it)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )

            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }

    override fun writeNotification(notificationMessage: NotificationMessage): Completable {
        return Completable.create { emitter ->
            cacheSource.writeCache(notificationMessage)
            val disposable = persistentSource.write(notificationMessage)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                emitter.onComplete()
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )


            emitter.setCancellable {
                disposable.dispose()
            }
        }
    }
}