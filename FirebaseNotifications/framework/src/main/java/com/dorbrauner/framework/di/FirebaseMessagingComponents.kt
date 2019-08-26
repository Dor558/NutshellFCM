package com.dorbrauner.framework.di

import android.app.Application
import android.app.NotificationManager
import com.dorbrauner.framework.NotificationsReceiversRegisterer
import com.dorbrauner.framework.application.ApplicationLifeCycleWrapper
import com.dorbrauner.framework.database.AppDatabase
import com.dorbrauner.framework.NotificationsFrameworkContract


internal object FirebaseMessagingComponents {

    lateinit var database: AppDatabase
    lateinit var cacheSource: NotificationsFrameworkContract.Sources.CacheSource
    lateinit var persistentSource: NotificationsFrameworkContract.Sources.PersistentSource
    lateinit var notificationsInteractor: NotificationsFrameworkContract.Interactor
    lateinit var notificationsRepository: NotificationsFrameworkContract.Repository
    lateinit var notificationsMessageReceiver: NotificationsFrameworkContract.NotificationsMessageReceiver
    lateinit var notificationBuilder: NotificationsFrameworkContract.AndroidNotificationBuilder
    lateinit var systemNotificationManager: NotificationManager
    lateinit var notificationsManager: NotificationsFrameworkContract.AndroidNotificationsManager
    lateinit var importanceTranslator: NotificationsFrameworkContract.Translators.ImportanceTranslator
    lateinit var notificationNotifier: NotificationsFrameworkContract.NotificationsNotifier
    lateinit var notificationsWriter: NotificationsFrameworkContract.NotificationMessageWriter
    lateinit var notificationsConsumer: NotificationsFrameworkContract.NotificationsConsumer
    lateinit var casesManager: NotificationsFrameworkContract.NotificationsHandling.CasesManager
    lateinit var notificationsReceiversRegisterer: NotificationsReceiversRegisterer

    fun init(application: Application,
             notificationsFactory: NotificationsFrameworkContract.AndroidNotificationsFactory,
             casesProvider: NotificationsFrameworkContract.NotificationsHandling.CasesProvider): Boolean {
        val applicationContext = Injections.provideApplicationContext(application)
        database = Injections.provideDatabase(application)
        systemNotificationManager = Injections.provideNotificationsManager(application)
        cacheSource = Injections.provideCacheSource()
        persistentSource = Injections.providePersistentSource(database)
        notificationsInteractor = Injections.provideNotificationInteractor(persistentSource, cacheSource)
        notificationsRepository = Injections.provideNotificationRepository(notificationsInteractor)
        importanceTranslator = Injections.provideImpotenceTranslator()
        notificationsManager = Injections.provideNotificationsManager(systemNotificationManager, importanceTranslator)
        notificationBuilder = Injections.provideNotificationBuilder(applicationContext, notificationsManager, notificationsFactory)
        notificationsMessageReceiver = Injections.provideNotificationsMessageReceiver(applicationContext, notificationsRepository, notificationBuilder)
        notificationsWriter = Injections.provideNotificationsNotificationsWriter(notificationsRepository)
        notificationNotifier = Injections.provideNotificationsNotifier(application, notificationsWriter)
        casesManager = Injections.provideCaseManager(casesProvider)
        notificationsConsumer = Injections.provideNotificationsConsumer(systemNotificationManager, notificationsRepository, casesManager)
        notificationsReceiversRegisterer = Injections.provideNotificationReceiversRegisterer(ApplicationLifeCycleWrapper(application), application, notificationsConsumer)
        return true
    }

}