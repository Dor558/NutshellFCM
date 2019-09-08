package com.nutshellfcm.rxworkframework


interface Cancelable {

    val isCancelled: Boolean
    fun cancel()
}