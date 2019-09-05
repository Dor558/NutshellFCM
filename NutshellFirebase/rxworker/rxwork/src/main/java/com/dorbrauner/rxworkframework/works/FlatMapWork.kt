package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.Observer
import com.dorbrauner.rxworkframework.scheudlers.Scheduler
import com.dorbrauner.rxworkframework.works.runnables.WorkMapRunnable


internal open class FlatMapWork<T,S>(
    mapRunnable: WorkMapRunnable<T, S>,
    private val parentWork: ParentWork<T>
) : Work<S>(mapRunnable), ParentWork<S> {

    override val compositeCancellable: CompositeCancellable = parentWork.compositeCancellable

    private val parentObserver = Observer<T>(
        onResult = {
            mapRunnable.input = it
            compositeCancellable
                .append(parentWork
                        .observeOnScheduler.schedule(mapRunnable))
        },

        onError = { throwable ->
            observers.forEach { it.onError.invoke(throwable) }
        }
    )

    init {
        subscribeOnScheduler = parentWork.subscribeOnScheduler
        observeOnScheduler = parentWork.observeOnScheduler
    }

    override fun onResult(result: S) {
        flatMapWork(result)
    }

    @Suppress("UNCHECKED_CAST")
    private fun flatMapWork(mappedWork: S) {
        (mappedWork as Work<S>).subscribeOn(parentWork.subscribeOnScheduler)
        (mappedWork as Work<S>).observeOn(observeOnScheduler)
        compositeCancellable.append((mappedWork as Work<S>).subscribeOnObservers(observers,
                                                                                 parentWork.observeOnScheduler))
    }

    override fun onWorkCancelled() {
        parentWork.onWorkCancelled()
    }

    override fun subscribe(onResult: (S) -> Unit, onError: (Throwable) -> Unit): Cancelable {
        observers.add(Observer(onResult, onError))
        parentWork.subscribe(parentObserver.onResult, parentObserver.onError)
        return compositeCancellable
    }

    override fun subscribeOn(scheduler: Scheduler): ScheduledWork<S> {
        parentWork.subscribeOn(scheduler)
        return super.subscribeOn(scheduler)
    }

    override fun doOnSubscribe(onSubscribe: () -> Unit): ScheduledWork<S> {
        parentWork.doOnSubscribe(onSubscribe)
        return this
    }

    override fun doOnCancelled(onCancelled: () -> Unit): ScheduledWork<S> {
        parentWork.doOnCancelled(onCancelled)
        return this
    }
}