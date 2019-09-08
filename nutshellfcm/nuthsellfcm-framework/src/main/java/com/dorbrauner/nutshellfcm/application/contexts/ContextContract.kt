package com.dorbrauner.nutshellfcm.application.contexts

import android.content.Context


interface ContextContract {

    interface AndroidContext {
        fun get(): Context
        fun getApplicationContext(): ApplicationContext
    }

}