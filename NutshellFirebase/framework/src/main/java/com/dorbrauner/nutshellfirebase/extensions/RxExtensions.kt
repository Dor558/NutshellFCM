package com.dorbrauner.nutshellfirebase.extensions

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.internal.functions.Functions
import io.reactivex.subjects.BehaviorSubject

/**
 * Error handling
 *
 * subscribeBy - must consumeNotificationsMessages error
 * subscribeIgnoreError - ignored error, use with care, if you want to consumeNotificationsMessages an error later better implement subscribeBy and mark onError as a TODO\n
 * subscribeNoErrorExpected - this event source should not throw an error (i.e. behaviour subject, wrapSafly), method will crash when an error occurs
 */
fun <T> Observable<T>.subscribeBy(onError: (Throwable) -> Unit, onComplete: () -> Unit = {}, onNext: (T) -> Unit = {}) =
        subscribe(
                onNext,
                onError,
                onComplete
        )

/**
 * @see Observable.subscribeBy
 */
fun <T> Observable<T>.subscribeIgnoreError(onComplete: () -> Unit = {}, onNext: (T) -> Unit = {}) =
        subscribeBy(
                onNext = onNext,
                onError = { Log.e(TAG, "Ignored Error") },
                onComplete = onComplete
        )

/**
 * @see subscribeBy
 */
fun <T> Observable<T>.subscribeNoErrorExpected(onComplete: () -> Unit = {}, onNext: (T) -> Unit = {}) =
        subscribeBy(
                onNext = onNext,
                onComplete = onComplete,
                onError = { Functions.ON_ERROR_MISSING.accept(it) }
        )

/**
 * @see subscribeBy
 */
fun Completable.subscribeNoErrorExpected(onComplete: () -> Unit = {}) =
        subscribeBy(
                onComplete = onComplete,
                onError = { Functions.ON_ERROR_MISSING.accept(it) }
        )

/**
 * @see subscribeBy
 */
fun <T> Flowable<T>.subscribeBy(onError: (Throwable) -> Unit, onComplete: () -> Unit = {}, onNext: (T) -> Unit = {}) =
        subscribe(
                onNext,
                onError,
                onComplete
        )

/**
 * @see subscribeBy
 */
fun <T> Maybe<T>.subscribeBy(onError: (Throwable) -> Unit, onComplete: () -> Unit = {}, onNext: (T) -> Unit = {}) =
        subscribe(
                onNext,
                onError,
                onComplete
        )

/**
 * @see subscribeBy
 */
fun <T> Maybe<T>.subscribeIgnoreError(onComplete: () -> Unit = {}, onNext: (T) -> Unit = {}) =
        subscribeBy(
                onNext = onNext,
                onError = { Log.e(TAG, "Ignored Error") },
                onComplete = onComplete
        )

/**
 * @see subscribeBy
 */
fun <T> Single<T>.subscribeNoErrorExpected(onSuccess: (T) -> Unit = {}) =
        subscribeBy(
                onSuccess = onSuccess,
                onError = { Functions.ON_ERROR_MISSING.accept(it) }
        )

/**
 * @see subscribeBy
 */
fun <T> Single<T>.subscribeBy(onError: (Throwable) -> Unit, onSuccess: (T) -> Unit = {}) =
        subscribe(
                onSuccess,
                onError
        )

/**
 * @see subscribeBy
 */
fun <T> Single<T>.subscribeIgnoreError(onSuccess: (T) -> Unit = {}) =
        subscribeBy(
                onSuccess = onSuccess,
                onError = { Log.e(TAG, "Ignored Error") }
        )

/**
 * @see subscribeBy
 */
fun Completable.subscribeBy(onError: (Throwable) -> Unit, onComplete: () -> Unit = {}) =
        subscribe(
                onComplete,
                onError
        )

fun <T> Observable<T>.doOnEmpty(onEmpty: () -> Unit) =
        switchIfEmpty(Observable.empty<T>().doOnComplete { onEmpty.invoke() })

fun Completable.toBinarySingle() =
        toSingleDefault(true).onErrorReturnItem(false)

fun <R ,T: Iterable<R>> Observable<T>.toIterable(): Observable<R> {
        return flatMapIterable { it }
}

fun <T> BehaviorSubject<T>.onUniqueNext(value: T) {
        if (this.value != value) {
                this.onNext(value)
        }
}

@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
inline fun <reified R : Any> Observable<*>.filterIsInstance(): Observable<R> = ofType(R::class.java)


fun Completable.blockingGetOrThrow() {
        blockingGet()?.let { throw it }
}

fun <T, R> Observable<T>.mapOrEmpty(mapper: (T) -> R?): Observable<R> {
        return this.switchMap { item ->
                mapper.invoke(item)?.let {
                        Observable.just<R>(it)
                } ?: Observable.empty<R>()
        }
}

fun <T> BehaviorSubject<T>.onNextUnique(newValue: T) {
        if(value != newValue) onNext(newValue)
}

sealed class BoxedValue<T> {
        class Empty<T>: BoxedValue<T>()
        data class Value<T>(val value: T): BoxedValue<T>()
}

fun <T> Observable<BoxedValue<T>>.extractBoxedValue() =
        filterIsInstance<BoxedValue.Value<T>>()
                .map { it.value }




