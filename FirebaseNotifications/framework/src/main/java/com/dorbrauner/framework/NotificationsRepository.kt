package com.dorbrauner.framework

import com.dorbrauner.framework.extensions.subscribeBy
import com.dorbrauner.framework.NotificationsFrameworkContract.Repository.NotificationMessage
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class NotificationsRepository(private val notificationsInteractor: NotificationsFrameworkContract.Interactor)
    : NotificationsFrameworkContract.Repository,
    NotificationsFrameworkContract.NotificationMessageWriter {

    override fun write(notificationMessage: NotificationMessage): Completable {
        return Completable.create { emitter ->
            notificationsInteractor.writeNotification(notificationMessage)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                emitter.onComplete()
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun read(): Single<List<NotificationMessage>> {
        return Single.create { emitter ->
            notificationsInteractor.readNotifications()
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onSuccess = {
                                emitter.onSuccess(it)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun read(id: String): Single<NotificationMessage> {
        return Single.create { emitter ->
            notificationsInteractor
                    .readNotification(id)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onSuccess = {
                                emitter.onSuccess(it)
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun purge(): Completable {
        return Completable.create { emitter ->
            notificationsInteractor
                    .purgeNotifications()
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                emitter.onComplete()
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }

    override fun remove(id: String): Completable {
        return Completable.create { emitter ->
            notificationsInteractor
                    .removeNotification(id)
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(
                            onComplete = {
                                emitter.onComplete()
                            },
                            onError = {
                                emitter.onError(it)
                            }
                    )
        }
    }
}