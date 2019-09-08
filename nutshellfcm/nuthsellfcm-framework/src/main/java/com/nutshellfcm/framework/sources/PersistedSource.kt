package com.nutshellfcm.framework.sources

import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.model.NotificationMessage
import com.nutshellfcm.common.PersistentAdapterContract
import com.nutshellfcm.rxworkframework.RxWorkCreator
import com.nutshellfcm.rxworkframework.works.ScheduledWork


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