package com.dorbrauner.nutshellfirebase.application.contexts

import android.content.Context


interface ContextContract {

    interface AndroidContext {
        fun get(): Context
        fun getApplicationContext(): ApplicationContext
    }

}