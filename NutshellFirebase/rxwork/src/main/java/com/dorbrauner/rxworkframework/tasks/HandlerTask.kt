package com.dorbrauner.rxworkframework.tasks

import android.os.Handler
import com.dorbrauner.rxworkframework.Cancelable

class HandlerTask(private val runnable: Runnable,
                  private val handler: Handler) : Runnable, Cancelable {

    override var isCancelled: Boolean = false

    override fun run() {
        if (!isCancelled) {
            runnable.run()
        }
    }

    override fun cancel() {
        handler.removeCallbacks(this)
        isCancelled = true
    }
}