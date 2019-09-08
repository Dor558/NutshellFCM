package com.nutshellfcm.rxworkframework

import com.nutshellfcm.rxworkframework.works.ScheduledWork
import com.nutshellfcm.rxworkframework.works.Work
import com.nutshellfcm.rxworkframework.works.WorkEmitter
import com.nutshellfcm.rxworkframework.works.runnables.WorkRunnable


object RxWorkCreator {

    fun <T> create(workEmitterInvokable: (WorkEmitter<T>) -> Unit): ScheduledWork<T> {
        return Work(WorkRunnable(workEmitterInvokable))
    }
}