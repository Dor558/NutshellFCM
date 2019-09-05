package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.Observer

internal class NotifyObserversRunnable<T>(private val observers: Set<Observer<T>>): Runnable {

    var result: T? = null

    var throwable: Throwable? = null

    override fun run() {
        if (result != null) {
            observers.forEach { it.onResult(result!!) }
        }

        if (throwable != null) {
            observers.forEach { it.onError(throwable!!) }
        }
    }
}