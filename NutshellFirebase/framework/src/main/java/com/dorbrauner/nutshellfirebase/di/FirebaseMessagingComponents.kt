package com.dorbrauner.nutshellfirebase.di

import android.app.Application
import android.app.NotificationManager
import com.dorbrauner.nutshellfirebase.NotificationsReceiversRegisterer
import com.dorbrauner.nutshellfirebase.application.ApplicationLifeCycleWrapper
import com.dorbrauner.nutshellfirebase.database.Database
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract


internal object FirebaseMessagingComponents {

    lateinit var database: Database
    lateinit var cacheSource: NutshellFirebaseContract.Sources.CacheSource
    lateinit var persistentSource: NutshellFirebaseContract.Sources.PersistentSource
    lateinit var notificationsInteractor: NutshellFirebaseContract.Interactor
    lateinit var notificationsRepository: NutshellFirebaseContract.Repository
    lateinit var notificationsMessageRouter: NutshellFirebaseContract.NotificationsMessageRouter
    lateinit var notificationBuilder: NutshellFirebaseContract.AndroidNotificationBuilder
    lateinit var systemNotificationManager: NotificationManager
    lateinit var notificationsManager: NutshellFirebaseContract.AndroidNotificationsManager
    lateinit var importanceTranslator: NutshellFirebaseContract.Translators.ImportanceTranslator
    lateinit var notificationNotifier: NutshellFirebaseContract.NotificationsNotifier
    lateinit var notificationsWriter: NutshellFirebaseContract.NotificationMessageWriter
    lateinit var notificationsConsumer: NutshellFirebaseContract.NotificationsConsumer
    lateinit var casesManager: NutshellFirebaseContract.NotificationsHandling.CasesManager
    lateinit var notificationsReceiversRegisterer: NotificationsReceiversRegisterer
    lateinit var androidNotificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory

    fun init(application: Application,
             notificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory,
             casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider,
             foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder): Boolean {
        val applicationContext = Injections.provideApplicationContext(application)
        androidNotificationsFactory = notificationsFactory
        database = Injections.provideDatabase(application)
        systemNotificationManager = Injections.provideNotificationsManager(application)
        cacheSource = Injections.provideCacheSource()
        persistentSource = Injections.providePersistentSource(database)
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