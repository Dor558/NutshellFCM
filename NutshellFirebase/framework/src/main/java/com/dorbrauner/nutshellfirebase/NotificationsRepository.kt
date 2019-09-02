package com.dorbrauner.nutshellfirebase

import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
import com.dorbrauner.rxworkframework.RxWork
import com.dorbrauner.rxworkframework.scheudlers.Schedulers
import com.dorbrauner.rxworkframework.works.ScheduledWork

internal class NotificationsRepository(private val notificationsInteractor: NutshellFirebaseContract.Interactor)
    : NutshellFirebaseContract.Repository,
    NutshellFirebaseContract.NotificationMessageWriter {

    override fun write(notificationMessage: NotificationMessage): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            notificationsInteractor.writeNotification(notificationMessage)
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

    override fun read(): ScheduledWork<List<NotificationMessage>> {
        return RxWork.create { emitter ->
            notificationsInteractor.readNotifications()
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

    override fun read(id: String): ScheduledWork<NotificationMessage> {
        return RxWork.create { emitter ->
            notificationsInteractor
                .readNotification(id)
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

    override fun purge(): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            notificationsInteractor
                    .purgeNotifications()
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

    override fun remove(id: String): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            notificationsInteractor
                    .removeNotification(id)
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

    override fun remove(ids: List<String>): ScheduledWork<Unit> {
        return RxWork.create { emitter ->
            notificationsInteractor
                .removeNotifications(ids)
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