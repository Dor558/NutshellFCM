package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.scheudlers.Scheduler
import com.dorbrauner.rxworkframework.works.runnables.EmittedRunnable


internal interface ParentWork<T>: ScheduledWork<T> {
    var subscribeOnScheduler: Scheduler
    var observeOnScheduler: Scheduler
    val compositeCancellable: CompositeCancellable
    val emittedRunnable: EmittedRunnable<T>

    fun onWorkCancelled()
}