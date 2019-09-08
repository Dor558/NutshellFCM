package com.nutshellfcm.framework.application.contexts

import android.content.Context


interface ContextContract {

    interface AndroidContext {
        fun get(): Context
        fun getApplicationContext(): ApplicationContext
    }

}