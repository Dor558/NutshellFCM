package com.dorbrauner.rxworkframework

import com.dorbrauner.rxworkframework.works.ScheduledWork
import com.dorbrauner.rxworkframework.works.Work
import com.dorbrauner.rxworkframework.works.WorkEmitter
import com.dorbrauner.rxworkframework.works.runnables.WorkRunnable


object RxWorkCreator {

    fun <T> create(workEmitterInvokable: (WorkEmitter<T>) -> Unit): ScheduledWork<T> {
        return Work(WorkRunnable(workEmitterInvokable))
    }
}