package com.dorbrauner.rxworkframework.tasks

import com.dorbrauner.rxworkframework.Cancelable
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Future


internal class FutureTask(private val runnable: Runnable) : Callable<Unit> , Cancelable {

    lateinit var future: Future<Unit>

    override val isCancelled by lazy { future.isCancelled }

    override fun call() {
        runnable.run()
    }

    override fun cancel() {
        future.cancel(true)
    }
}