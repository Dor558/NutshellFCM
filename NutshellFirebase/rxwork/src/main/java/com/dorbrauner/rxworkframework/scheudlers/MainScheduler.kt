package com.dorbrauner.rxworkframework.scheudlers

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.dorbrauner.rxworkframework.Cancelable
import com.dorbrauner.rxworkframework.tasks.HandlerTask


class MainScheduler: Scheduler {

    private val handler = Handler(Looper.getMainLooper())

    override fun start() {}

    override fun shutdown() {}

    override fun schedule(runnable: Runnable): Cancelable {
        val handlerWork = HandlerTask(runnable, handler)
        val message = Message.obtain(handler, handlerWork)
        if (Build.VERSION.SDK_INT > 22) {
            message.isAsynchronous = false
        }

        handler.sendMessage(message)
        return handlerWork
    }

    override fun blockingSchedule(runnable: Runnable) {
        schedule(runnable)
    }
}