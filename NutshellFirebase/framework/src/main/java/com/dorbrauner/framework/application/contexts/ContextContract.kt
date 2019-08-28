package com.dorbrauner.framework.application.contexts

import android.content.Context
import com.dorbrauner.framework.application.contexts.ApplicationContext


interface ContextContract {

    interface AndroidContext {
        fun get(): Context
        fun getApplicationContext(): ApplicationContext
    }

}