package com.dorbrauner.nutshellfcm

import android.os.Handler
import android.os.HandlerThread


internal object NutshellHandler: Handler(HandlerThread("NutshellHandler", Thread.MAX_PRIORITY)
    .also { it.start() }.looper)