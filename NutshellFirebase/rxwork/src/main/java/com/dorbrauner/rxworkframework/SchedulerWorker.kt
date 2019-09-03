package com.dorbrauner.rxworkframework

import com.dorbrauner.rxworkframework.tasks.FutureTask
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit


class SchedulerWorker(threadFactory: ThreadFactory) {

    private val executor = ScheduledThreadPoolExecutor(1, threadFactory)

    fun schedule(runnable: Runnable): Cancelable {
        val futureWork = FutureTask(runnable)
        val future = executor.submit(futureWork)
        futureWork.future = future
        return futureWork
    }

    fun shutdown() {
        runCatching {
            while (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow()
            }
        }.getOrElse {
            executor.shutdownNow()
        }
    }

}