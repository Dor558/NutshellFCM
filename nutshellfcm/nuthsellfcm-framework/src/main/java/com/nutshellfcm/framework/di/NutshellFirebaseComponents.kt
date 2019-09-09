package com.nutshellfcm.framework.di

import android.app.Application
import android.app.NotificationManager
import com.nutshellfcm.framework.NotificationsReceiversRegisterer
import com.nutshellfcm.framework.application.ApplicationLifeCycleWrapper
import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.common.PersistentAdapterContract


internal object NutshellFirebaseComponents {

    lateinit var cacheSource: NutshellFCMContract.Sources.CacheSource
    lateinit var notificationsInteractor: NutshellFCMContract.Interactor
    lateinit var notificationsRepository: NutshellFCMContract.Repository
    lateinit var notificationsMessageRouter: NutshellFCMContract.NotificationsMessageRouter
    lateinit var notificationBuilder: NutshellFCMContract.AndroidNotificationBuilder
    lateinit var systemNotificationManager: NotificationManager
    lateinit var notificationsManager: NutshellFCMContract.AndroidNotificationsManager
    lateinit var importanceTranslator: NutshellFCMContract.Translators.ImportanceTranslator
    lateinit var notificationNotifier: NutshellFCMContract.NotificationsNotifier
    lateinit var notificationsWriter: NutshellFCMContract.NotificationMessageWriter
    lateinit var notificationsConsumer: NutshellFCMContract.NotificationsConsumer
    lateinit var casesManager: NutshellFCMContract.CasesManager
    lateinit var notificationsReceiversRegisterer: NotificationsReceiversRegisterer
    lateinit var androidNotificationsFactory: NutshellFCMContract.AndroidNotificationsFactory
    lateinit var handledNotificationsNotifier: NutshellFCMContract.HandledNotificationsNotifier
    lateinit var persistedMessageToNotificationMessageConverter: NutshellFCMContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter
    var persistedSource: NutshellFCMContract.Sources.PersistedSource? = null

    fun init(application: Application,
             notificationsFactory: NutshellFCMContract.AndroidNotificationsFactory,
             casesProvider: NutshellFCMContract.CasesProvider,
             foregroundServicesBinder: NutshellFCMContract.ForegroundServicesBinder,
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