package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.works.WorkEmitter

internal class WorkRunnable<T>(
    val workEmitterInvokable: (WorkEmitter<T>) -> Unit) :
    EmittedRunnable<T> {

    override var emitter: WorkEmitter<T>? = null

    override fun run() {
        runCatching {
            workEmitterInvokable.invoke(emitter!!)
        }.getOrElse {
            emitter!!.onError(it)
        }

    }
}