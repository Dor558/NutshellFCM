package com.dorbrauner.nutshellfirebase.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.dorbrauner.nutshellfirebase.*
import com.dorbrauner.nutshellfirebase.application.ApplicationLifeCycleWrapper
import com.dorbrauner.nutshellfirebase.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfirebase.ImportanceTranslator
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.sources.CacheSource
import com.dorbrauner.nutshellfirebase.sources.PersistedSource
import com.dorbrauner.persistentadapters.PersistentAdapterContract


internal object Injections {

    fun provideApplicationContext(application: Application): ApplicationContext {
        return ApplicationContext(application)
    }

    fun provideNotificationsManager(application: Application): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun providePersistetMessageToNotificaitonMessageConverter(): NutshellFirebaseContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter {
        return PersistedMessageToNotificationMessageConverter()
    }

    fun providePersistentSource(
        persistedAdapter: PersistentAdapterContract.Adapter,
        converter: NutshellFirebaseContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter): NutshellFirebaseContract.Sources.PersistedSource? {
        return PersistedSource(persistedAdapter, converter)
    }

    fun provideCacheSource(): NutshellFirebaseContract.Sources.CacheSource {
        return CacheSource()
    }

    fun provideNotificationInteractor(
        persistedSource: NutshellFirebaseContract.Sources.PersistedSource?,
        cacheSource: NutshellFirebaseContract.Sources.CacheSource
    ): NutshellFirebaseContract.Interactor {
        return NotificationsInteractor(persistedSource, cacheSource)
    }

    fun provideNotificationRepository(notificationsInteractor: NutshellFirebaseContract.Interactor): NutshellFirebaseContract.Repository {
        return NotificationsRepository(notificationsInteractor)
    }

    fun provideNotificationsHanlder(): NutshellFirebaseContract.NotificationsHandling.HandledNotificationsNotifier {
        return NutshellNotificationHandler
    }

    fun provideCaseManager(casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider,
                           handledNotificationsNotifier: NutshellFirebaseContract.NotificationsHandling.HandledNotificationsNotifier): NutshellFirebaseContract.NotificationsHandling.CasesManager {
        return NotificationCasesManager(casesProvider, handledNotificationsNotifier)
    }

    fun provideNotificationsConsumer(
        applicationContext: ApplicationContext,
        notificationManager: NotificationManager,
        foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder,
        notificationsRepository: NutshellFirebaseContract.Repository,
        casesManager: NutshellFirebaseContract.NotificationsHandling.CasesManager
    ): NutshellFirebaseContract.NotificationsConsumer {
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
        notificationsConsumer: NutshellFirebaseContract.NotificationsConsumer
    ): NotificationsReceiversRegisterer {
        return NotificationsReceiversRegisterer(applicationLifeCycleWrapper, application, notificationsConsumer)
    }

    fun provideNotificationBuilder(
        applicationContext: ApplicationContext,
        foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder,
        androidNotificationsManager: NutshellFirebaseContract.AndroidNotificationsManager,
        notificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory
    ): NutshellFirebaseContract.AndroidNotificationBuilder {
        return AndroidNotificationBuilder(
            applicationContext,
            foregroundServicesBinder,
            androidNotificationsManager,
            notificationsFactory
        )
    }

    fun provideNotificationsMessageReceiver(
        applicationContext: ApplicationContext,
        notificationsRepository: NutshellFirebaseContract.Repository,
        notificationBuilder: NutshellFirebaseContract.AndroidNotificationBuilder
    ): NutshellFirebaseContract.NotificationsMessageRouter {
        return NotificationsMessageRouter(applicationContext, notificationsRepository, notificationBuilder)
    }

    fun provideImpotenceTranslator(): NutshellFirebaseContract.Translators.ImportanceTranslator {
        return ImportanceTranslator()
    }

    fun provideNotificationsManager(
        notificationManager: NotificationManager,
        importanceTranslator: NutshellFirebaseContract.Translators.ImportanceTranslator
    ): NutshellFirebaseContract.AndroidNotificationsManager {
        return AndroidNotificationsManager(notificationManager, importanceTranslator)
    }

    fun provideNotificationsNotificationsWriter(notificationsRepository: NutshellFirebaseContract.Repository): NutshellFirebaseContract.NotificationMessageWriter {
        return notificationsRepository as NutshellFirebaseContract.NotificationMessageWriter
    }

    fun provideNotificationsNotifier(
        application: Application,
        notificationMessageWriter: NutshellFirebaseContract.NotificationMessageWriter
    ): NutshellFirebaseContract.NotificationsNotifier {
        return NotificationNotifier(application, notificationMessageWriter)
    }
}