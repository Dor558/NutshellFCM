package com.dorbrauner.framework.di

import android.app.Application
import android.app.NotificationManager
import com.dorbrauner.framework.NotificationsReceiversRegisterer
import com.dorbrauner.framework.application.ApplicationLifeCycleWrapper
import com.dorbrauner.framework.database.Database
import com.dorbrauner.framework.NotificationsFrameworkContract


internal object FirebaseMessagingComponents {

    lateinit var Database: Database
    lateinit var cacheSource: NotificationsFrameworkContract.Sources.CacheSource
    lateinit var persistentSource: NotificationsFrameworkContract.Sources.PersistentSource
    lateinit var notificationsInteractor: NotificationsFrameworkContract.Interactor
    lateinit var notificationsRepository: NotificationsFrameworkContract.Repository
    lateinit var notificationsMessageRouter: NotificationsFrameworkContract.NotificationsMessageRouter
    lateinit var notificationBuilder: NotificationsFrameworkContract.AndroidNotificationBuilder
    lateinit var systemNotificationManager: NotificationManager
    lateinit var notificationsManager: NotificationsFrameworkContract.AndroidNotificationsManager
    lateinit var importanceTranslator: NotificationsFrameworkContract.Translators.ImportanceTranslator
    lateinit var notificationNotifier: NotificationsFrameworkContract.NotificationsNotifier
    lateinit var notificationsWriter: NotificationsFrameworkContract.NotificationMessageWriter
    lateinit var notificationsConsumer: NotificationsFrameworkContract.NotificationsConsumer
    lateinit var casesManager: NotificationsFrameworkContract.NotificationsHandling.CasesManager
    lateinit var notificationsReceiversRegisterer: NotificationsReceiversRegisterer
    lateinit var androidNotificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory

    fun init(application: Application,
             notificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory,
             casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider,
             foregroundServicesBinder: NotificationsFrameworkContract.ForegroundServicesBinder): Boolean {
        val applicationContext = Injections.provideApplicationContext(application)
        androidNotificationsFactory = notificationsFactory
        Database = Injections.provideDatabase(application)
        systemNotificationManager = Injections.provideNotificationsManager(application)
        cacheSource = Injections.provideCacheSource()
        persistentSource = Injections.providePersistentSource(Database)
        notificationsInteractor = Injections.provideNotificationInteractor(persistentSource, cacheSource)
        notificationsRepository = Injections.provideNotificationRepository(notificationsInteractor)
        importanceTranslator = Injections.provideImpotenceTranslator()
        notificationsManager = Injections.provideNotificationsManager(systemNotificationManager, importanceTranslator)
        notificationBuilder = Injections.provideNotificationBuilder(
            applicationContext,
            foregroundServicesBinder,
            notificationsManager,
            notificationsFactory
        )
        notificationsMessageRouter = Injections.provideNotificationsMessageReceiver(
            applicationContext,
            notificationsRepository,
            notificationBuilder
        )
        notificationsWriter = Injections.provideNotificationsNotificationsWriter(notificationsRepository)
        notificationNotifier = Injections.provideNotificationsNotifier(application, notificationsWriter)
        casesManager = Injections.provideCaseManager(casesProvider)
        notificationsConsumer = Injections.provideNotificationsConsumer(
            applicationContext,
            systemNotificationManager,
            foregroundServicesBinder,
            notificationsRepository,
            casesManager
        )
        notificationsReceiversRegisterer = Injections.provideNotificationReceiversRegisterer(
            ApplicationLifeCycleWrapper(application),
            application,
            notificationsConsumer
        )
        return true
    }

}