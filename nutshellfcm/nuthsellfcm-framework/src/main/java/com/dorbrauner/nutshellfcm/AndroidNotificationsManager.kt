package com.dorbrauner.nutshellfcm

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.dorbrauner.nutshellfcm.extensions.toBundle

internal class AndroidNotificationsManager(private val notificationManager: NotificationManager,
                                                private val importanceTranslator: NutshellFCMContract.Translators.ImportanceTranslator
)
    : NutshellFCMContract.AndroidNotificationsManager {

    override fun show(context: Context, androidNotification: NutshellFCMContract.AndroidNotification) {
        val extras = androidNotification.payload.toBundle()


        val soundUri = androidNotification.channel.soundUri

        val builder = NotificationCompat.Builder(context, androidNotification.channel.id).apply {
            setSmallIcon(androidNotification.smallIcon)
            setContentTitle(androidNotification.contentTitle)
            setContentText(androidNotification.contentText)
            setAutoCancel(true)
            priority = importanceTranslator.getAndroidPriority(androidNotification.importance)
            setContentIntent(androidNotification.contentIntent)
            setDeleteIntent(androidNotification.deleteIntent)
            addExtras(extras)

            soundUri?.let {
                setSound(it, AudioManager.STREAM_NOTIFICATION)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            androidNotification.channel.let {
                val channelId = androidNotification.channel.id
                val channelName = androidNotification.channel.channelName
                val importance = importanceTranslator.getAndroidImportance(androidNotification.importance)
                val notificationChannel = setNotificationChannel(
                        channelId,
                        channelName,
                        importance,
                        soundUri
                )
                builder.setChannelId(notificationChannel.id)
            }
        }

        notificationManager.notify(androidNotification.id, builder.build())
    }


    override fun showForeground(service: Service, androidNotification: NutshellFCMContract.AndroidNotification) {
        val notificationBuilder = NotificationCompat.Builder(service, "default_importance")
                .setContentTitle(androidNotification.contentTitle)
                .setContentText(androidNotification.contentText)
                .setColor(androidNotification.color)
                .setPriority(importanceTranslator.getAndroidPriority(androidNotification.importance))
                .setLargeIcon(androidNotification.largeIcon)
                .setSmallIcon(androidNotification.smallIcon)
                .setContentIntent(androidNotification.contentIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = androidNotification.channel.id
            val channelName = androidNotification.channel.channelName
            val importance = importanceTranslator.getAndroidImportance(androidNotification.importance)
            val soundUri = androidNotification.channel.soundUri
            val notificationChannel = setNotificationChannel(channelId, channelName, importance, soundUri)

            notificationBuilder.setChannelId(notificationChannel.id)
        }


        service.startForeground(androidNotification.id, notificationBuilder.build())
    }


    @TargetApi(Build.VERSION_CODES.O)
    private fun setNotificationChannel(channelId: String,
                                       channelName: String,
                                       importance: Int,
                                       soundUri: Uri?): NotificationChannel {
        val notificationChannel = NotificationChannel(channelId, channelName, importance)

        if (soundUri == null) {
            notificationChannel.setSound(null, null)
        } else {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build()

            notificationChannel.setSound(soundUri, audioAttributes)
        }

        notificationManager.createNotificationChannel(notificationChannel)
        return notificationChannel
    }

}