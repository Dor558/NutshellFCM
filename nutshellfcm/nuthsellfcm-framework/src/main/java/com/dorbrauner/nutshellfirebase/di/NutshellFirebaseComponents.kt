package com.dorbrauner.nutshellfirebase.di

import android.app.Application
import android.app.NotificationManager
import com.dorbrauner.nutshellfirebase.NotificationsReceiversRegisterer
import com.dorbrauner.nutshellfirebase.application.ApplicationLifeCycleWrapper
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.persistentadapters.PersistentAdapterContract


internal object NutshellFirebaseComponents {

    lateinit var cacheSource: NutshellFirebaseContract.Sources.CacheSource
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
    lateinit var handledNotificationsNotifier: NutshellFirebaseContract.NotificationsHandling.HandledNotificationsNotifier
    lateinit var persistedMessageToNotificationMessageConverter: NutshellFirebaseContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter
    var persistedSource: NutshellFirebaseContract.Sources.PersistedSource? = null

    fun init(application: Application,
             notificationsFactory: NutshellFirebaseContract.AndroidNotificationsFactory,
             casesProvider: NutshellFirebaseContract.NotificationsHandling.CasesProvider,
             foregroundServicesBinder: NutshellFirebaseContract.ForegroundServicesBinder,
             persistentAdapter: PersistentAdapterContract.Adapter?): Boolean {
        val applicationContext = Injections.provideApplicationContext(application)
        androidNotificationsFactory = notificationsFactory
        systemNotificationManager = Injections.provideNotificationsManager(application)
        cacheSource = Injections.provideCacheSource()
        persistedMessageToNotificationMessageConverter = Injections.providePersistetMessageToNotificaitonMessageConverter()
        persistentAdapter?.let {
            persistedSource = Injections.providePersistentSource(persistentAdapter, persistedMessageToNotificationMessageConverter)
        }
        notificationsInteractor = Injections.provideNotificationInteractor(persistedSource, cacheSource)
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
        handledNotificationsNotifier = Injections.provideNotificationsHanlder()
        casesManager = Injections.provideCaseManager(casesProvider, handledNotificationsNotifier)
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