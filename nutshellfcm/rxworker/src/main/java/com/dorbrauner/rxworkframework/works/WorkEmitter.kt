package com.dorbrauner.rxworkframework.works

interface WorkEmitter<T> {
    fun onResult(result: T)
    fun onError(error: Throwable)
}