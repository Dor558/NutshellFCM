package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.Observer
import com.dorbrauner.rxworkframework.CancelledObserver
import com.dorbrauner.rxworkframework.Subscriber


internal class NotifyOnCancelledRunnable(private val cancelledObserver: Set<CancelledObserver>,
                                private val observers: MutableSet<Observer<*>>,
                                private val subscriber: MutableSet<Subscriber>): Runnable {

    override fun run() {
        cancelledObserver.forEach { it.onCancelled.invoke() }
        observers.clear()
        subscriber.clear()
    }
}