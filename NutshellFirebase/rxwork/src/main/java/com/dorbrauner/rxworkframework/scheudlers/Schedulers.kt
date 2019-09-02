package com.dorbrauner.rxworkframework.scheudlers

import com.dorbrauner.rxworkframework.scheudlers.FuturesScheduler.Companion.Type.*


object Schedulers {

    val main get() =  MainScheduler()
    val single get() =  FuturesScheduler(FIXED_SINGLE).apply { start() }
    val unbounded get() =  FuturesScheduler(UNBOUNDED_CACHE).apply { start() }
    val bounded get() = FuturesScheduler(BOUNDED_CACHE).apply { start() }

}


