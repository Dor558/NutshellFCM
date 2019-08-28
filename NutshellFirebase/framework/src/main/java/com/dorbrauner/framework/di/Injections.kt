package com.dorbrauner.framework.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.dorbrauner.framework.*
import com.dorbrauner.framework.application.ApplicationLifeCycleWrapper
import com.dorbrauner.framework.application.contexts.ApplicationContext
import com.dorbrauner.framework.database.Database
import com.dorbrauner.framework.database.DATABASE_NAME
import com.dorbrauner.framework.ImportanceTranslator
import com.dorbrauner.framework.NotificationsFrameworkContract
import com.dorbrauner.framework.sources.CacheSource
import com.dorbrauner.framework.sources.PersistentSource


internal object Injections {

    fun provideApplicationContext(application: Application): ApplicationContext {
        return ApplicationContext(application)
    }

    fun provideNotificationsManager(application: Application): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun provideDatabase(application: Application): Database {
        return Room.databaseBuilder(application, Database::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun providePersistentSource(roomDatabase: Database): NotificationsFrameworkContract.Sources.PersistentSource {
        return PersistentSource(roomDatabase)
    }

    fun provideCacheSource(): NotificationsFrameworkContract.Sources.CacheSource {
        return CacheSource()
    }

    fun provideNotificationInteractor(persistentSource: NotificationsFrameworkContract.Sources.PersistentSource,
                                      cacheSource: NotificationsFrameworkContract.Sources.CacheSource): NotificationsFrameworkContract.Interactor {
        return NotificationsInteractor(persistentSource, cacheSource)
    }

    fun provideNotificationRepository(notificationsInteractor: NotificationsFrameworkContract.Interactor): NotificationsFrameworkContract.Repository {
        return NotificationsRepository(notificationsInteractor)
    }

    fun provideCaseManager(casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider): NotificationsFrameworkContract.NotificationsHandling.CasesManager {
        return NotificationCasesManager(casesProvider)
    }

    fun provideNotificationsConsumer(applicationContext: ApplicationContext,
                                     notificationManager: NotificationManager,
                                     foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder,
                                     notificationsRepository: NotificationsFrameworkContract.Repository,
                                     casesManager: NotificationsFrameworkContract.NotificationsHandling.CasesManager): NotificationsFrameworkContract.NotificationsConsumer {
        return NotificationsConsumer(applicationContext, notificationManager, notificationsRepository, foregroundServicesBinder, casesManager)
    }

    fun provideNotificationReceiversRegisterer(applicationLifeCycleWrapper: ApplicationLifeCycleWrapper,
                                               application: Application,
                                               notificationsConsumer: NotificationsFrameworkContract.NotificationsConsumer): NotificationsReceiversRegisterer {
        return NotificationsReceiversRegisterer(applicationLifeCycleWrapper, application, notificationsConsumer)
    }

    fun provideNotificationBuilder(applicationContext: ApplicationContext,
                                   foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder,
                                   androidNotificationsManager: NotificationsFrameworkContract.AndroidNotificationsManager,
                                   notificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory): NotificationsFrameworkContract.AndroidNotificationBuilder {
        return AndroidNotificationBuilder(applicationContext, foregroundServicesBinder, androidNotificationsManager, notificationsFactory)
    }

    fun provideNotificationsMessageReceiver(applicationContext: ApplicationContext,
                                            notificationsRepository: NotificationsFrameworkContract.Repository,
                                            notificationBuilder: NotificationsFrameworkContract.AndroidNotificationBuilder): NotificationsFrameworkContract.NotificationsMessageRouter {
        return NotificationsMessageRouter(applicationContext, notificationsRepository, notificationBuilder)
    }

    fun provideImpotenceTranslator(): NotificationsFrameworkContract.Translators.ImportanceTranslator {
        return ImportanceTranslator()
    }

    fun provideNotificationsManager(notificationManager: NotificationManager,
                                    importanceTranslator: NotificationsFrameworkContract.Translators.ImportanceTranslator): NotificationsFrameworkContract.AndroidNotificationsManager {
        return AndroidNotificationsManager(notificationManager, importanceTranslator)
    }

    fun provideNotificationsNotificationsWriter(notificationsRepository: NotificationsFrameworkContract.Repository): NotificationsFrameworkContract.NotificationMessageWriter {
        return notificationsRepository as NotificationsFrameworkContract.NotificationMessageWriter
    }

    fun provideNotificationsNotifier(application: Application,
                                     notificationMessageWriter: NotificationsFrameworkContract.NotificationMessageWriter): NotificationsFrameworkContract.NotificationsNotifier {
        return NotificationNotifier(application, notificationMessageWriter)
    }
}