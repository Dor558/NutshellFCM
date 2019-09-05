package com.dorbrauner.rxworkframework.scheudlers

import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.RxWorkerThreadFactory
import com.dorbrauner.rxworkframework.tasks.FutureTask
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference


class FuturesScheduler(private val type: Type) : Scheduler {

    private val executor = AtomicReference<ExecutorService>()

    companion object {

        enum class Type {
            BOUNDED_CACHE,
            UNBOUNDED_CACHE,
            FIXED_SINGLE
        }
    }

    @Synchronized
    override fun start() {
        var next: ExecutorService? = null
        val current = executor.get()
        if (current == null) {
            next = executorInstance()
        }

        if (executor.compareAndSet(current, next)) {
            return
        }
    }

    private fun executorInstance(): ExecutorService {
        return when (type) {
            Companion.Type.BOUNDED_CACHE -> Executors.newFixedThreadPool(5, RxWorkerThreadFactory("Bounded"))
            Companion.Type.UNBOUNDED_CACHE ->  Executors.newCachedThreadPool(RxWorkerThreadFactory("Unbound"))
            Companion.Type.FIXED_SINGLE ->  Executors.newFixedThreadPool(1, RxWorkerThreadFactory("Single"))
        }
    }


    @Synchronized
    override fun shutdown() {
        val current: ExecutorService? = executor.get()
        runCatching {
            while (current?.awaitTermination(800, TimeUnit.MILLISECONDS) == false) {
                current.shutdownNow()
            }

        }.getOrElse {
            current?.shutdownNow()
        }
    }

    @Synchronized
    override fun schedule(runnable: Runnable): Cancelable {
        val futureWork = FutureTask(runnable)
        val future = executor.get().submit(futureWork)
        futureWork.future = future
        return futureWork
    }
}