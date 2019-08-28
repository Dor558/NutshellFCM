package com.dorbrauner.example_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dorbrauner.nutshellfirebase.R
import com.dorbrauner.framework.LocalMessagesNotifier
import com.dorbrauner.framework.NotificationsFrameworkContract.*
import com.dorbrauner.framework.database.model.NotificationMessage


class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity_layout)
    }


    override fun onStart() {
        super.onStart()
        LocalMessagesNotifier.notify(
            NotificationMessage(
                "Action 4",
                NotificationType.FOREGROUND_NOTIFICATION
            )
        )
    }


    override fun onStop() {
        super.onStop()
        LocalMessagesNotifier.notifyDismiss("Action 4")
    }

}