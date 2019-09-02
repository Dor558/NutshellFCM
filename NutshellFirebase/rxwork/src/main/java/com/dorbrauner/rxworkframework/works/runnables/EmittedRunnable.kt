package com.dorbrauner.rxworkframework.works.runnables

import com.dorbrauner.rxworkframework.works.WorkEmitter


internal interface EmittedRunnable<T>: Runnable {
    var emitter: WorkEmitter<T>?
}