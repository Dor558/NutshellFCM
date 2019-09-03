package com.dorbrauner.nutshellfirebase

import android.os.Handler
import android.os.HandlerThread


object NutshellHandler: Handler(HandlerThread("NutshellHandler", Thread.MAX_PRIORITY)
    .also { it.start() }.looper)