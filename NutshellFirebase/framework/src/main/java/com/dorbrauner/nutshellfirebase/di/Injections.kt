package com.dorbrauner.nutshellfirebase.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.dorbrauner.nutshellfirebase.*
import com.dorbrauner.nutshellfirebase.application.ApplicationLifeCycleWrapper
import com.dorbrauner.nutshellfirebase.application.contexts.ApplicationContext
import com.dorbrauner.nutshellfirebase.database.Database
import com.dorbrauner.nutshellfirebase.database.DATABASE_NAME
import com.dorbrauner.nutshellfirebase.ImportanceTranslator
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.sources.CacheSource
import com.dorbrauner.nutshellfirebase.sources.PersistentSource


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

    fun providePersistentSource(roomDatabase: Database): NutshellFirebaseContract.Sources.PersistentSource {
        return PersistentSource(roomDatabase)
    }

    fun provideCacheSource(): NutshellFirebaseContract.Sources.CacheSource {
        return CacheSource()
    }

    fun provideNotificationInteractor(persistentSource: NutshellFirebaseContract.Sources.PersistentSource,
                                      cacheSource: NutshellFirebaseContract.Sources.CacheSource): NutshellFirebaseContract.Interactor {
        return NotificationsInteractor(persistentSource, cacheSource)
    }

    fun provideNotificationRepository(notificationsInteractor: NutshellFirebaseContract.Interactor): NutshellFirebaseContract.Repository {
        return NotificationsRepository(notificationsInteractor)
    }

    fun provideCaseManager(casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider): NutshellFirebaseContract.NotificationsHandling.CasesManager {
        return NotificationCasesManager(casesProvider)
    }

    fun provideNotificationsConsumer(applicationContext: ApplicationContext,
                                     notificationManager: NotificationManager,
                                     foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder,
                                     notificationsRepository: NutshellFirebaseContract.Repository,
                                     casesManager: NutshellFirebaseContract.NotificationsHandling.CasesManager): NutshellFirebaseContract.NotificationsConsumer {
        return NotificationsConsumer(applicationContext, notificationManager, notificationsRepository, foregroundServicesBinder, casesManager)
    }

    fun provideNotificationReceiversRegisterer(applicationLifeCycleWrapper: ApplicationLifeCycleWrapper,
                                               application: Application,
                                               notificationsConsumer: NutshellFirebaseContract.NotificationsConsumer): NotificationsReceiversRegisterer {
        return NotificationsReceiversRegisterer(applicationLifeCycleWrapper, application, notificationsConsumer)
    }

    fun provideNotificationBuilder(applicationContext: ApplicationContext,
                                   foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder,
                                   androidNotificationsManager: NutshellFirebaseContract.AndroidNotificationsManager,
                                   notificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory): NutshellFirebaseContract.AndroidNotificationBuilder {
        return AndroidNotificationBuilder(applicationContext, foregroundServicesBinder, androidNotificationsManager, notificationsFactory)
    }

    fun provideNotificationsMessageReceiver(applicationContext: ApplicationContext,
                                            notificationsRepository: NutshellFirebaseContract.Repository,
                                            notificationBuilder: NutshellFirebaseContract.AndroidNotificationBuilder): NutshellFirebaseContract.NotificationsMessageRouter {
        return NotificationsMessageRouter(applicationContext, notificationsRepository, notificationBuilder)
    }

    fun provideImpotenceTranslator(): NutshellFirebaseContract.Translators.ImportanceTranslator {
        return ImportanceTranslator()
    }

    fun provideNotificationsManager(notificationManager: NotificationManager,
                                    importanceTranslator: NutshellFirebaseContract.Translators.ImportanceTranslator): NutshellFirebaseContract.AndroidNotificationsManager {
        return AndroidNotificationsManager(notificationManager, importanceTranslator)
    }

    fun provideNotificationsNotificationsWriter(notificationsRepository: NutshellFirebaseContract.Repository): NutshellFirebaseContract.NotificationMessageWriter {
        return notificationsRepository as NutshellFirebaseContract.NotificationMessageWriter
    }

    fun provideNotificationsNotifier(application: Application,
                                     notificationMessageWriter: NutshellFirebaseContract.NotificationMessageWriter): NutshellFirebaseContract.NotificationsNotifier {
        return NotificationNotifier(application, notificationMessageWriter)
    }
}