package com.nutshellfcm.rxworkframework.works

import com.nutshellfcm.rxworkframework.scheudlers.Scheduler
import com.nutshellfcm.rxworkframework.works.runnables.EmittedRunnable


internal interface ParentWork<T>: ScheduledWork<T> {
    var subscribeOnScheduler: Scheduler
    var observeOnScheduler: Scheduler
    val compositeCancellable: CompositeCancellable
    val emittedRunnable: EmittedRunnable<T>

    fun onWorkCancelled()
}