package com.nutshellfcm.sample_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.nutshellfcm.framework.NutshellLocalMessagesNotifier
import com.nutshellfcm.framework.NutshellFCMContract
import com.nutshellfcm.framework.NutshellNotificationHandler
import com.nutshellfcm.framework.model.NotificationMessage
import kotlinx.android.synthetic.main.example_activity_layout.*


class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity_layout)

        val sb = StringBuilder()
        NutshellNotificationHandler
            .handledNotifications
            .observe(this, Observer { handledNotifications ->
            text_view_messages.text = sb.append("handled $handledNotifications").append("\n")
        })
    }

    override fun onResume() {
        super.onResume()
        NutshellLocalMessagesNotifier.notifyDismiss("Action 4")
    }


    override fun onPause() {
        super.onPause()
        NutshellLocalMessagesNotifier.notify(
            NotificationMessage(
                "Action 4",
                NutshellFCMContract.NotificationType.FOREGROUND_NOTIFICATION
            )
        )
    }

}