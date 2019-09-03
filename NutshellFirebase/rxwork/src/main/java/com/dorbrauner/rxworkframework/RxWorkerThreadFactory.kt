package com.dorbrauner.rxworkframework

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong


class RxWorkerThreadFactory(val schedulerName: String) : ThreadFactory {

    private val counter = AtomicLong()

    private val KEY_PRIORITY = "rxworker.priority"

     private val priority get() =  Math.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY,
             Integer.getInteger(KEY_PRIORITY, Thread.NORM_PRIORITY)!!)
     )

    override fun newThread(r: Runnable): Thread {
        val thread = CustomThread(r,"$schedulerName RxWorker Thread - ${counter.incrementAndGet()}")
        thread.isDaemon = true
        thread.priority = priority
        return thread
    }

    inner class CustomThread(r: Runnable, threadName: String) : Thread(r, threadName)
}