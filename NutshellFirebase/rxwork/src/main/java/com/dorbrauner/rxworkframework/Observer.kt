package com.dorbrauner.rxworkframework


internal data class Observer<T>(val onResult: (T) -> Unit, val onError: (Throwable) -> Unit)