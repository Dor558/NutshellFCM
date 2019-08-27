package com.dorbrauner.framework


class DefaultForegroundBinder : NotificationsFrameworkContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? = null
}