package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.works.WorkEmitter


internal class WorkFilterRunnable<T>(private val filterInvokable: (T) -> Boolean): EmittedRunnable<T> {

    override var emitter: WorkEmitter<T>? = null

    var input: T? = null

    override fun run() {
        runCatching {
            val isFiltered = filterInvokable.invoke(input!!)
            if (isFiltered) {
                emitter!!.onResult(input!!)
            }

        }.getOrElse {
            emitter!!.onError(it)
        }
    }
}