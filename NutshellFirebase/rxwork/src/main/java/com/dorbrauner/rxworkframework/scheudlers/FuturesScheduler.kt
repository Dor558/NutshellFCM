package com.dorbrauner.rxworkframework.scheudlers

import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.tasks.FutureTask
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicReference


class FuturesScheduler(private val type: Type) : Scheduler {

    private val executor = AtomicReference<ExecutorService>()

    companion object {
        val SHUTDOWN: ExecutorService = Executors.newFixedThreadPool(1)
            .apply { shutdown() }

        enum class Type {
            BOUNDED_CACHE,
            UNBOUNDED_CACHE,
            FIXED_SINGLE
        }
    }

    override fun start() {
        var next: ExecutorService? = null
        while (true) {
            val current = executor.get()
            if (current != null && current != SHUTDOWN) {
                next?.shutdown()
                return
            }

            if (next == null) {
                next = executorInstance()
            }

            if (executor.compareAndSet(current, next)) {
                return
            }
        }
    }

    private fun executorInstance(): ExecutorService {
        return when (type) {
            Companion.Type.BOUNDED_CACHE -> Executors.newFixedThreadPool(5)
            Companion.Type.UNBOUNDED_CACHE ->  Executors.newCachedThreadPool()
            Companion.Type.FIXED_SINGLE ->  Executors.newFixedThreadPool(1)
        }
    }


    override fun shutdown() {
        var current = executor.get()
        if (current != SHUTDOWN) {
            current = executor.getAndSet(SHUTDOWN)
            if (current != SHUTDOWN) {
                current.shutdownNow()
            }
        }
    }

    override fun schedule(runnable: Runnable): Cancelable {
        val futureWork = FutureTask(runnable)
        val future = executor.get().submit(futureWork)
        futureWork.future = future
        return futureWork
    }

    override fun blockingSchedule(runnable: Runnable) {
        val futureWork = FutureTask(runnable)
        val future = executor.get().submit(futureWork)
        futureWork.future = future
        future.get()
    }
}