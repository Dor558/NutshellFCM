package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.works.runnables.NotifyObserversRunnable
import com.dorbrauner.rxworkframework.works.runnables.WorkMapRunnable


internal class MapWork<T,S>(
    workMapRunnable: WorkMapRunnable<T, S>,
    parentWork: ParentWork<T>
)
    : FlatMapWork<T, S>(workMapRunnable, parentWork), ParentWork<S> {

    override fun onResult(result: S) {
        mapWork(result)
    }

    private fun mapWork(result: S) {
        val notifyObserversRunnable = NotifyObserversRunnable(observers)
        notifyObserversRunnable.result = result
        observeOnScheduler.schedule(notifyObserversRunnable)
    }

}