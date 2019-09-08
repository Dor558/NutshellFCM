package com.nutshellfcm.rxworkframework.works.runnables

import com.nutshellfcm.rxworkframework.Observer
import com.nutshellfcm.rxworkframework.CancelledObserver
import com.nutshellfcm.rxworkframework.Subscriber


internal class NotifyOnCancelledRunnable(private val cancelledObserver: Set<CancelledObserver>,
                                private val observers: MutableSet<Observer<*>>,
                                private val subscriber: MutableSet<Subscriber>): Runnable {

    override fun run() {
        cancelledObserver.forEach { it.onCancelled.invoke() }
        observers.clear()
        subscriber.clear()
    }
}