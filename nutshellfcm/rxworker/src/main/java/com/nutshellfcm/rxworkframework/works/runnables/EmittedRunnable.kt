package com.nutshellfcm.rxworkframework.works.runnables

import com.nutshellfcm.rxworkframework.works.WorkEmitter


internal interface EmittedRunnable<T>: Runnable {
    var emitter: WorkEmitter<T>?
}