package com.dorbrauner.example_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dorbrauner.firebasenotifications.R
import com.dorbrauner.framework.LocalMessagesNotifier
import com.dorbrauner.framework.NotificationsFrameworkContract


class ExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.example_activity_layout)

        LocalMessagesNotifier.notify(NotificationsFrameworkContract.Repository.NotificationMessage(
            "Action 4",
                    NotificationsFrameworkContract.NotificationType.FOREGROUND_NOTIFICATION)
        )
    }


    override fun onStop() {
        super.onStop()
        LocalMessagesNotifier.notifyDismiss("Action 4")
    }

}