package com.dorbrauner.example_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dorbrauner.nutshellfirebase.LocalMessagesNotifier
import com.dorbrauner.nutshellfirebase.NutshellFirebaseContract
import com.dorbrauner.nutshellfirebase.NutshellNotificationHandler
import com.dorbrauner.nutshellfirebase.R
import com.dorbrauner.nutshellfirebase.database.model.NotificationMessage
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
        LocalMessagesNotifier.notifyDismiss("Action 4")
    }


    override fun onPause() {
        super.onPause()
        LocalMessagesNotifier.notify(
            NotificationMessage(
                "Action 4",
                NutshellFirebaseContract.NotificationType.FOREGROUND_NOTIFICATION
            )
        )
    }

}