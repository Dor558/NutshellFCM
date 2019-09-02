package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.scheudlers.Scheduler

interface ScheduledWork<T> {
    fun subscribe(onResult: (T) -> Unit = {}, onError: (Throwable) -> Unit = {}): Cancelable
    fun blockingGet(): T?
    fun <S> flatMap(transition: (T) -> ScheduledWork<S>): ScheduledWork<S>
    fun <S> map(transition: (T) -> S): ScheduledWork<S>
    fun filter(filter: (T) -> Boolean): ScheduledWork<T>
    fun subscribeOn(scheduler: Scheduler): ScheduledWork<T>
    fun observeOn(scheduler: Scheduler): ScheduledWork<T>
    fun doOnSubscribe(onSubscribe: () -> Unit): ScheduledWork<T>
    fun doOnCancelled(onCancelled: () -> Unit): ScheduledWork<T>
}