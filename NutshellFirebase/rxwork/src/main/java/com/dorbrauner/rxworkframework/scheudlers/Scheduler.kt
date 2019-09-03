package com.dorbrauner.rxworkframework.scheudlers

import com.dorbrauner.rxworkframework.Cancelable


interface Scheduler {

    fun start()
    fun shutdown()
    fun schedule(runnable: Runnable): Cancelable
}