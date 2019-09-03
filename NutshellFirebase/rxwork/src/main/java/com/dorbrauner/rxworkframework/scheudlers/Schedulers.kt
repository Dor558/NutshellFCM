package com.dorbrauner.rxworkframework.scheudlers

import com.dorbrauner.rxworkframework.scheudlers.FuturesScheduler.Companion.Type.*

object Schedulers {

    val main = MainScheduler().apply { start() }
    val single =  FuturesScheduler(FIXED_SINGLE).apply { start() }
    val unbounded =  FuturesScheduler(UNBOUNDED_CACHE).apply { start() }
    val bounded = FuturesScheduler(BOUNDED_CACHE).apply { start() }
}


