package com.dorbrauner.nutshellfcm.sources

import com.dorbrauner.nutshellfcm.NutshellFCMContract
import com.dorbrauner.nutshellfcm.model.NotificationMessage
import com.dorbrauner.persistentadapters.PersistentAdapterContract
import com.dorbrauner.rxworkframework.RxWorkCreator
import com.dorbrauner.rxworkframework.works.ScheduledWork


internal class PersistedSource(
    private val persistentAdapter: PersistentAdapterContract.Adapter,
    private val converter: NutshellFCMContract.Sources.PersistedSource.PersistedMessageToNotificationMessageConverter
) : NutshellFCMContract.Sources.PersistedSource
{

    override fun purge(): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            runCatching {
                emitter.onResult(persistentAdapter.purge())
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun remove(id: String): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            runCatching {
                emitter.onResult(persistentAdapter.remove(id))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun remove(ids: List<String>): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            runCatching {
                emitter.onResult(persistentAdapter.removeGroup(ids))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun read(): ScheduledWork<List<NotificationMessage>> {
        return RxWorkCreator.create { emitter ->
            runCatching {
                emitter.onResult(converter.convert(persistentAdapter.get()))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun read(id: String): ScheduledWork<NotificationMessage>{
        return RxWorkCreator.create { emitter ->
            runCatching {
                emitter.onResult(converter.convert(persistentAdapter.get(id)))
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }

    override fun write(notificationMessage: NotificationMessage): ScheduledWork<Unit> {
        return RxWorkCreator.create { emitter ->
            runCatching {
                persistentAdapter.insert(converter.convertBack(notificationMessage))
                emitter.onResult(Unit)
            }.getOrElse {
                emitter.onError(it)
            }
        }
    }
}