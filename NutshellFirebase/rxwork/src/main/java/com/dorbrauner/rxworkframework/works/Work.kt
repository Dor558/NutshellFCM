package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.Observer
import com.dorbrauner.rxworkframework.scheudlers.Scheduler
import com.dorbrauner.rxworkframework.scheudlers.Schedulers
import com.dorbrauner.rxworkframework.CancelledObserver
import com.dorbrauner.rxworkframework.Subscriber
import com.dorbrauner.rxworkframework.works.runnables.*
import java.util.concurrent.atomic.AtomicReference


internal open class Work<T>(
    final override val emittedRunnable: EmittedRunnable<T>
) : ScheduledWork<T>, ParentWork<T>, WorkEmitter<T> {

    protected val observers: MutableSet<Observer<T>> = mutableSetOf()

    protected val subscribers: MutableSet<Subscriber> = mutableSetOf()

    protected val cancelledObservers: MutableSet<CancelledObserver> = mutableSetOf()

    final override var subscribeOnScheduler: Scheduler = Schedulers.unbounded

    final override var observeOnScheduler: Scheduler = subscribeOnScheduler

    protected val isScheduled = AtomicReference<Cancelable>()

    override val compositeCancellable: CompositeCancellable = CompositeCancellable(this)

    init {
        emittedRunnable.emitter = this
    }

    override fun onResult(result: T) {
        val notifyObserversRunnable = NotifyObserversRunnable(observers)
        notifyObserversRunnable.result = result
        observeOnScheduler.schedule(notifyObserversRunnable)
    }

    override fun onError(error: Throwable) {
        val notifyObserversRunnable = NotifyObserversRunnable(observers)
        notifyObserversRunnable.throwable = error
        observeOnScheduler.schedule(notifyObserversRunnable)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onWorkCancelled() {
        observeOnScheduler.schedule(
            NotifyOnCancelledRunnable(
            cancelledObservers,
            observers as MutableSet<Observer<*>>,
            subscribers
        )
        )
    }

    override fun subscribe(onResult: (T) -> Unit, onError: (Throwable) -> Unit): Cancelable {
        observers.add(Observer(onResult, onError))
        return subscribeActual(subscribeOnScheduler)
    }

    fun subscribeOnObservers(observers: Set<Observer<T>>, scheduler: Scheduler = observeOnScheduler): Cancelable {
        this.observers.addAll(observers)
        return subscribeActual(scheduler)
    }

    private fun subscribeActual(scheduler: Scheduler): CompositeCancellable {
        subscribers.forEach { it.onSubscribe() }

        if (isScheduled.get() == null) {
            val cancellable = scheduler.schedule(emittedRunnable)
            compositeCancellable.append(cancellable)
            isScheduled.getAndSet(cancellable)
        }

        return compositeCancellable
    }

    override fun blockingGet(): T? = BlockingWork(BlockingRunnable(emittedRunnable), this).blockingGet()

    override fun subscribeOn(scheduler: Scheduler): ScheduledWork<T> {
        this.subscribeOnScheduler = scheduler
        return this
    }

    override fun observeOn(scheduler: Scheduler): ScheduledWork<T> {
        this.observeOnScheduler = scheduler
        return this
    }

    override fun <S> map(transition: (T) -> S): ScheduledWork<S> {
        return MapWork(WorkMapRunnable(transition), this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <S> flatMap(transition: (T) -> ScheduledWork<S>): ScheduledWork<S> {
        return FlatMapWork(WorkMapRunnable(transition), this) as ScheduledWork<S>
    }

    override fun filter(filter: (T) -> Boolean): ScheduledWork<T> {
        return FilterWork(WorkFilterRunnable(filter), this)
    }

    override fun doOnSubscribe(onSubscribe: () -> Unit): ScheduledWork<T> {
        subscribers.add(Subscriber(onSubscribe))
        return this
    }

    override fun doOnCancelled(onCancelled: () -> Unit): ScheduledWork<T> {
        cancelledObservers.add(CancelledObserver(onCancelled))
        return this
    }
}