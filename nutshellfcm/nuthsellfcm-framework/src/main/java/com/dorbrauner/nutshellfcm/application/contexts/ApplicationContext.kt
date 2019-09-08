package com.dorbrauner.nutshellfcm.application.contexts

import android.content.Context

class ApplicationContext(private val context: Context) : ContextContract.AndroidContext {

    override fun get(): Context = context.applicationContext

    override fun getApplicationContext(): ApplicationContext {
        return ApplicationContext(context.applicationContext)
    }
}