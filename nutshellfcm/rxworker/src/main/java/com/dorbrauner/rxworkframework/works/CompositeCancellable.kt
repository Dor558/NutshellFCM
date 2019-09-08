package com.dorbrauner.rxworkframework.works

import com.dorbrauner.rxworkframework.Cancelable


internal class CompositeCancellable(private val parentWork: ParentWork<*>) : Cancelable {

    private val set: MutableSet<Cancelable> = mutableSetOf()

    override val isCancelled: Boolean = set.all { it.isCancelled }

    override fun cancel() {
        set.forEach { it.cancel() }
        set.clear()
        parentWork.onWorkCancelled()
    }

    fun append(cancelable: Cancelable) {
        set.add(cancelable)
    }
}