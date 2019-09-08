package com.nutshellfcm.rxworkframework.scheudlers

import com.nutshellfcm.rxworkframework.Cancelable


interface Scheduler {

    fun start()
    fun shutdown()
    fun schedule(runnable: Runnable): Cancelable
}