package com.dorbrauner.rxworkframework


internal data class Subscriber(val onSubscribe: () -> Unit)