package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.works.WorkEmitter


internal class WorkMapRunnable<T,S>(
    private val transition: (T) -> S
) : EmittedRunnable<S> {

    override var emitter: WorkEmitter<S>? = null
    var input: T? = null

    override fun run() {
        runCatching {
            val mappedScheduledWork = transition.invoke(input!!)
            emitter!!.onResult(mappedScheduledWork)
        }.getOrElse {
            emitter!!.onError(it)
        }
    }
}