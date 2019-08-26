package com.dorbrauner.framework.extensions

import android.os.Bundle


val Any.TAG: String get() = this::class.java.simpleName

fun Map<String, String>.toBundle(): Bundle {
    val bundle = Bundle()
    this.keys.forEach { key ->
        bundle.putString(key, this.getValue(key))
    }

    return bundle
}