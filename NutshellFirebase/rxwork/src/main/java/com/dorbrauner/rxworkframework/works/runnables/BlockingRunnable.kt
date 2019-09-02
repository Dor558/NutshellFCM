package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.works.WorkEmitter
import java.util.concurrent.CountDownLatch


internal class BlockingRunnable<T>(private val emittedRunnable: EmittedRunnable<T>)
    : EmittedRunnable<T>, WorkEmitter<T> {

    override var emitter: WorkEmitter<T>? = this

    internal var countDownLatch: CountDownLatch? = null

    internal var result: T? = null

    internal var error: Throwable? = null

    override fun run() {
        runCatching {
            emittedRunnable.emitter = this
            emittedRunnable.run()
        }.getOrElse { throwable ->
            emitter?.onError(throwable)
        }
    }

    override fun onResult(result: T) {
        this.result = result
        countDownLatch?.countDown()
    }

    override fun onError(error: Throwable) {
        this.error = error
        countDownLatch?.countDown()
    }
}