package com.nutshellfcm.framework.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.nutshellfcm.framework.*
import com.nutshellfcm.framework.application.ApplicationLifeCycleWrapper
import com.nutshellfcm.framework.application.contexts.ApplicationContext
import com.nutshellfcm.framework.ImportanceTranslator
import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.sources.CacheSource
import com.nutshellfcm.framework.sources.PersistedSource
import com.nutshellfcm.common.PersistentAdapterContract


internal object Injections {

    fun provideApplicationContext(application: Application): ApplicationContext {
        return ApplicationContext(application)
    }

    fun provideNotificationsManager(application: Application): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun providePersistetMessageToNotificaitonMessageConverter(): NutshellFCMContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter {
        return PersistedMessageToNotificationMessageConverter()
    }

    fun providePersistentSource(
        persistedAdapter: PersistentAdapterContract.Adapter,
        converter: NutshellFCMContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter): NutshellFCMContract.Sources.PersistedSource? {
        return PersistedSource(persistedAdapter, converter)
    }

    fun provideCacheSource(): NutshellFCMContract.Sources.CacheSource {
        return CacheSource()
    }

    fun provideNotificationInteractor(
        persistedSource: NutshellFCMContract.Sources.PersistedSource?,
        cacheSource: NutshellFCMContract.Sources.CacheSource
    ): NutshellFCMContract.Interactor {
        return NotificationsInteractor(persistedSource, cacheSource)
    }

    fun provideNotificationRepository(notificationsInteractor: NutshellFCMContract.Interactor): NutshellFCMContract.Repository {
        return NotificationsRepository(notificationsInteractor)
    }

    fun provideNotificationsHanlder(): NutshellFCMContract.HandledNotificationsNotifier {
        return NutshellNotificationHandler
    }

    fun provideCaseManager(casesProvider: NutshellFCMContract.CasesProvider,
                           handledNotificationsNotifier: NutshellFCMContract.HandledNotificationsNotifier
    ): NutshellFCMContract.CasesManager {
        return NotificationCasesManager(casesProvider, handledNotificationsNotifier)
    }

    fun provideNotificationsConsumer(
        applicationContext: ApplicationContext,
        notificationManager: NotificationManager,
        foregroundServicesBinder: NutshellFCMContract.ForegroundServicesBinder,
        notificationsRepository: NutshellFCMContract.Repository,
        casesManager: NutshellFCMContract.CasesManager
    ): NutshellFCMContract.NotificationsConsumer {
        return NotificationsConsumer(
            applicationContext,
            notificationManager,
            notificationsRepository,
            foregroundServicesBinder,
            casesManager
        )
    }

    fun provideNotificationReceiversRegisterer(
        applicationLifeCycleWrapper: ApplicationLifeCycleWrapper,
        application: Application,
        notificationsConsumer: NutshellFCMContract.NotificationsConsumer
    ): NotificationsReceiversRegisterer {
        return NotificationsReceiversRegisterer(applicationLifeCycleWrapper, application, notificationsConsumer)
    }

    fun provideNotificationBuilder(
        applicationContext: ApplicationContext,
        foregroundServicesBinder: NutshellFCMContract.ForegroundServicesBinder,
        androidNotificationsManager: NutshellFCMContract.AndroidNotificationsManager,
        notificationsFactory: NutshellFCMContract.AndroidNotificationsFactory
    ): NutshellFCMContract.AndroidNotificationBuilder {
        return AndroidNotificationBuilder(
            applicationContext,
            foregroundServicesBinder,
            androidNotificationsManager,
            notificationsFactory
        )
    }

    fun provideNotificationsMessageReceiver(
        applicationContext: ApplicationContext,
        notificationsRepository: NutshellFCMContract.Repository,
        notificationBuilder: NutshellFCMContract.AndroidNotificationBuilder
    ): NutshellFCMContract.NotificationsMessageRouter {
        return NotificationsMessageRouter(applicationContext, notificationsRepository, notificationBuilder)
    }

    fun provideImpotenceTranslator(): NutshellFCMContract.Translators.ImportanceTranslator {
        return ImportanceTranslator()
    }

    fun provideNotificationsManager(
        notificationManager: NotificationManager,
        importanceTranslator: NutshellFCMContract.Translators.ImportanceTranslator
    ): NutshellFCMContract.AndroidNotificationsManager {
        return AndroidNotificationsManager(notificationManager, importanceTranslator)
    }

    fun provideNotificationsNotificationsWriter(notificationsRepository: NutshellFCMContract.Repository): NutshellFCMContract.NotificationMessageWriter {
        return notificationsRepository as NutshellFCMContract.NotificationMessageWriter
    }

    fun provideNotificationsNotifier(
        application: Application,
        notificationMessageWriter: NutshellFCMContract.NotificationMessageWriter
    ): NutshellFCMContract.NotificationsNotifier {
        return NotificationNotifier(application, notificationMessageWriter)
    }
}