package com.dorbrauner.framework


class DefaultForegroundServicesBinder : NotificationsFrameworkContract.ForegroundServicesBinder {

    override fun bind(actionId: String): Class<*>? = null
}