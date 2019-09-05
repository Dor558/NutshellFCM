package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.Observer
import com.dorbrauner.rxworkframework.scheudlers.Scheduler
import com.dorbrauner.rxworkframework.works.runnables.WorkFilterRunnable


internal class FilterWork<T>(private val runnable: WorkFilterRunnable<T>,
                    private val parentWork: ParentWork<T>
) : Work<T>(runnable) {

    private val parentObserver = Observer<T>(
        onResult = {
            runnable.input = it
            compositeCancellable
                .append(parentWork
                    .observeOnScheduler.schedule(runnable))
        },

        onError = { throwable ->
            observers.forEach { it.onError.invoke(throwable) }
        }
    )

    override fun onWorkCancelled() {
        parentWork.onWorkCancelled()
    }

    override fun subscribe(onResult: (T) -> Unit, onError: (Throwable) -> Unit): Cancelable {
        observers.add(Observer(onResult, onError))
        parentWork.subscribe(parentObserver.onResult, parentObserver.onError)
        return compositeCancellable
    }

    override fun subscribeOn(scheduler: Scheduler): ScheduledWork<T> {
        parentWork.subscribeOn(scheduler)
        return super.subscribeOn(scheduler)
    }

    override fun doOnSubscribe(onSubscribe: () -> Unit): ScheduledWork<T> {
        parentWork.doOnSubscribe(onSubscribe)
        return this
    }

    override fun doOnCancelled(onCancelled: () -> Unit): ScheduledWork<T> {
        parentWork.doOnCancelled(onCancelled)
        return this
    }

}