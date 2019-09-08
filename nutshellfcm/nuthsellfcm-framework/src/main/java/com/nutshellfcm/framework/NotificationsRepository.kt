package com.nutshellfcm.framework

import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.rxworkframework.works.ScheduledWork

internal class NotificationsRepository(private val notificationsInteractor: NutshellFCMContract.Interactor) :
    NutshellFCMContract.Repository,
    NutshellFCMContract.NotificationMessageWriter {

    override fun write(notificationMessage: NotificationMessage): ScheduledWork<Unit> = notificationsInteractor.writeNotification(notificationMessage)

    override fun read(): ScheduledWork<List<NotificationMessage>> = notificationsInteractor.readNotifications()

    override fun read(id: String): ScheduledWork<NotificationMessage> = notificationsInteractor.readNotification(id)

    override fun purge(): ScheduledWork<Unit> = notificationsInteractor.purgeNotifications()

    override fun remove(id: String): ScheduledWork<Unit> = notificationsInteractor.removeNotification(id)

    override fun remove(ids: List<String>): ScheduledWork<Unit> = notificationsInteractor.removeNotifications(ids)
}