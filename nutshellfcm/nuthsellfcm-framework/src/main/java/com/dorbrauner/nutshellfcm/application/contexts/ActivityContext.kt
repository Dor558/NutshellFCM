package com.dorbrauner.nutshellfcm.application.contexts

import android.content.Context
import androidx.appcompat.app.AppCompatActivity


class ActivityContext(private val activity: AppCompatActivity): ContextContract.AndroidContext {

    override fun get(): Context = activity

    override fun getApplicationContext() = ApplicationContext(activity.applicationContext)
}