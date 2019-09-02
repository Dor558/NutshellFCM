package com.dorbrauner.rxworkframework


internal data class CancelledObserver(val onCancelled: () -> Unit)