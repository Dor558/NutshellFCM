package com.dorbrauner.rxworkframework


interface Cancelable {

    val isCancelled: Boolean
    fun cancel()
}