package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.Observer
import com.dorbrauner.rxworkframework.scheudlers.Scheduler
import com.dorbrauner.rxworkframework.works.runnables.BlockingRunnable
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.CountDownLatch


internal class BlockingWork<T>(private val blockingRunnable: BlockingRunnable<T>,
                      private val parentWork: ParentWork<T>) : Work<T>(blockingRunnable) {

    private val countDownLatch = CountDownLatch(1)

    override val compositeCancellable: CompositeCancellable = parentWork.compositeCancellable

    private val parentObserver = Observer<T>(
        onResult = {
            compositeCancellable.append(observeOnScheduler.schedule(blockingRunnable))
        },

        onError = { throwable ->
            observers.forEach { it.onError.invoke(throwable) }
        }
    )

    init {
        observeOnScheduler = parentWork.observeOnScheduler
        subscribeOnScheduler = parentWork.subscribeOnScheduler
        blockingRunnable.countDownLatch = countDownLatch
    }

    @Throws(Exception::class)
    override fun blockingGet(): T? {
        parentWork.subscribe(parentObserver.onResult, parentObserver.onError)
        runCatching {
            countDownLatch.await()
        }.getOrElse {
            Thread.currentThread().interrupt()
        }

        if (blockingRunnable.error != null) {
            throw RuntimeException("Failed to block and get value")
        }

        return blockingRunnable.result
    }

    override fun subscribeOn(scheduler: Scheduler): ScheduledWork<T> {
        parentWork.subscribeOn(scheduler)
        return super.subscribeOn(scheduler)
    }

    override fun doOnSubscribe(onSubscribe: () -> Unit): ScheduledWork<T> {
        parentWork.doOnSubscribe(onSubscribe)
        return this
    }

    override fun doOnCancelled(onCancelled: () -> Unit): ScheduledWork<T> {
        parentWork.doOnCancelled(onCancelled)
        return this
    }
}